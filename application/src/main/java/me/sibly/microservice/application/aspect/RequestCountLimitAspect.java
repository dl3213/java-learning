package me.sibly.microservice.application.aspect;


import lombok.extern.slf4j.Slf4j;
import me.sibly.microservice.application.annotation.RequestCountLimit;
import me.sibly.microservice.application.util.RequestUtils;
import me.sibyl.microservice.common.response.ResponseVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RequestCountLimitAspect {

    private static final String keyPrefix = "requestCountLimit-";

    @Resource
    private RedisTemplate redisTemplate;

    // 定义切点
    // 让所有有@LimitRequest注解的方法都执行切面方法
    @Pointcut("@annotation(requestCountLimit)")
    public void pointCut(RequestCountLimit requestCountLimit) {
    }

    @Around("pointCut(requestCountLimit)")
    public Object doAround(ProceedingJoinPoint pjp, RequestCountLimit requestCountLimit) throws Throwable {
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();

        try {
            // 获得request对象
            HttpServletRequest request = RequestUtils.getRequest();
            String sessionId = RequestUtils.getServletRequestAttributes().getSessionId();
            String key = keyPrefix + sessionId + "-" + request.getServletPath();
            log.info("[限制请求次数切片处理]redis-key: {} ", key);

            // 从缓存中获取请求次数
            Integer count = opsForValue.get(key);
            log.info("[限制请求次数切片处理]剩余请求次数: {} ", count);

            if(Objects.isNull(count)) {//null -> 第一次请求
                opsForValue.set(key, requestCountLimit.count() - 1, requestCountLimit.time(), TimeUnit.SECONDS);
            }else {
                if(count == 0){// 0 -> 次数限制
                    return new ResponseVO(800200,"接口请求超过次数");
                }else {// -1 保存
                    // SETRANGE?
                    opsForValue.increment (key, - 1 );
                }
            }
            Object result = pjp.proceed();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            log.error("[限制请求次数切片处理]验证限制请求次数时出现未知异常!");
            return new ResponseVO(800500,"验证限制请求次数时出现未知异常!");
        }
    }

}