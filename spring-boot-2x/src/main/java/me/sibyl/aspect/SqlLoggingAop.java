package me.sibyl.aspect;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatAroundSubmit;
import me.sibyl.annotation.SqlLogging;
import me.sibyl.common.response.Response;
import me.sibyl.listener.SibylEvent;
import me.sibyl.listener.SqlLoggingEvent;
import me.sibyl.service.AsyncTaskService;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Classname SqlLoggingAop
 * @Description SqlLoggingAop
 * @Date 2023/4/19 15:57
 * @Author by Qin Yazhi
 */
@Aspect
@Component
@Slf4j
public class SqlLoggingAop {

//    @Pointcut("@annotation(sqlLogging)")
//    public void pointCut(SqlLogging sqlLogging) {
//    }

    @Pointcut("execution(public * me.sibyl.dao..*())")
    public void pointCut() {
    }

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private AsyncTaskService asyncTaskService;

    @Resource
    private ApplicationContext applicationContext;


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println(Thread.currentThread().getName() + " in aop");
        Object proceed = pjp.proceed();
        //applicationContext.publishEvent(new SqlLoggingEvent(this, pjp, Thread.currentThread()));

        //https://blog.csdn.net/sdzhangshulong/article/details/104393244
        Collection<MappedStatement> mappedStatements = SqlSessionUtils
                .getSqlSession(sqlSessionFactory)
                .getConfiguration()
                .getMappedStatements();
        System.err.println(mappedStatements.size());
        mappedStatements
                .forEach(item -> {
                    System.err.println(item.getId());
                });
        return proceed;
//        //version1
//        try {
//            Object proceed = pjp.proceed();
//            return proceed;
//        } finally {// todo 或者事件监听???
//            asyncTaskService.sqlLogging(pjp, sqlLogging);
//        }
    }

}
