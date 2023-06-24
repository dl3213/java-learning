package me.sibyl.aspect;


import com.alibaba.fastjson2.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatAroundSubmit;
import me.sibyl.annotation.NoRepeatBeforeSubmit;
import me.sibyl.common.response.Response;
import me.sibyl.util.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class NoRepeatSubmitBeforeAspect {

    public static final String keyPrefix = "noRepeatSubmit-";

    @Setter
    private static int redisState = 1;

    @Resource
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(submit)")
    public void pointCut(NoRepeatBeforeSubmit submit) {
    }

    @Before("pointCut(submit)")
    public void before(JoinPoint jp, NoRepeatBeforeSubmit submit) {
        log.info(String.valueOf(submit));
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        //构建缓存key

        String noRepeatSubmitKey = AopJoinPointUtil.getCacheKeyByTarget(
                keyPrefix,
                jp,
                submit.mode(),
                submit.watchClass(),
                submit.classParamName()
        );
        Integer ret = opsForValue.get(noRepeatSubmitKey);
        log.info("[请求重复切片处理]cache-key: {} , value:{}", noRepeatSubmitKey, ret);

        // 如果缓存中有这个key视为重复提交
        if (Objects.nonNull(ret)) {
            log.error("[请求重复切片处理]重复提交");
            throw new RuntimeException(submit.msg());
        }
        opsForValue.set(noRepeatSubmitKey, 1, submit.expire(), submit.timeUnit());

    }

}
