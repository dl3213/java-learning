package me.sibyl.aspect;


import com.alibaba.fastjson2.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatSubmit;
import me.sibyl.common.response.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Classname NoRepeatSubmitAspect
 * @Description 重复提交处理切片，参数校验需@Validated，同时校验请求限制次数
 * @Date 2022/3/29 14:14
 * @Author by Qin Yazhi
 */
@Aspect
@Component
@Slf4j
@Order(12)
public class NoRepeatSubmitAspect {

    public static final String keyPrefix = "noRepeatSubmit-";

    @Setter
    private static int redisState = 1;

    @Resource
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        try {
            if (redisState != 1) throw new RedisConnectionFailureException("redis不可用");

            //构建缓存key
            String noRepeatSubmitKey = AopJoinPointUtil.getCacheKeyByTarget(keyPrefix, pjp, noRepeatSubmit.watchClass(), noRepeatSubmit.classParamName());

            String ret = opsForValue.get(noRepeatSubmitKey);
            log.info("[请求重复切片处理]redis-key: {} , value:{}", noRepeatSubmitKey, ret);

            // 如果缓存中有这个key视为重复提交
            if (Objects.nonNull(ret)) {
                log.error("[请求重复切片处理]重复提交");
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                // 被切的方法
                Method method = methodSignature.getMethod();
                // 返回类型
                Class<?> methodReturnType = method.getReturnType();
                return JSONObject.parseObject(ret, methodReturnType);
            }

            Object o = pjp.proceed();
            opsForValue.set(noRepeatSubmitKey, JSONObject.toJSONString(o), noRepeatSubmit.expire(), TimeUnit.SECONDS);
            return o;
        } catch (RedisConnectionFailureException redisException) {
            redisState = 0;
            log.error("redis不可用");
            Object o = pjp.proceed();
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("[请求重复切片处理]验证重复提交时出现未知异常");
            return Response.error(800500, "验证重复提交时出现未知异常");
        }
    }


}
