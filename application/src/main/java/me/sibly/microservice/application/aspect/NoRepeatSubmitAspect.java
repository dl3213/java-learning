package me.sibly.microservice.application.aspect;


import lombok.extern.slf4j.Slf4j;
import me.sibly.microservice.application.annotation.NoRepeatSubmit;
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
import java.util.concurrent.TimeUnit;

/**
 * @Classname NoRepeatSubmitAspect
 * @Description 重复提交处理切片
 * @Date 2022/3/29 14:14
 * @Author by Qin Yazhi
 */
@Aspect
@Component
@Slf4j
public class NoRepeatSubmitAspect {

    private static final String keyPrefix = "noRepeatSubmit-";

    @Resource
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        try {
            HttpServletRequest request = RequestUtils.getRequest();
            String sessionId = RequestUtils.getServletRequestAttributes().getSessionId();
            String key = keyPrefix +  sessionId + "-" + request.getServletPath();
            log.info("[请求重复切片处理]redis-key: {} ", key);
            if (opsForValue.get(key) == null) {// 如果缓存中有这个url视为重复提交
                Object o = pjp.proceed();
                opsForValue.set(key, 0, noRepeatSubmit.expire(), TimeUnit.SECONDS);
                return o;
            } else {
                log.error("[请求重复切片处理]重复提交");
                return new ResponseVO(800200,"重复请求");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("[请求重复切片处理]验证重复提交时出现未知异常!");
            return new ResponseVO(800500,"验证重复提交时出现未知异常!");
        }
    }

}
