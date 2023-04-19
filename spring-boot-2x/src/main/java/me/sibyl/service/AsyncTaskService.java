package me.sibyl.service;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.SqlLogging;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Classname AsyncTaskService
 * @Description AsyncTaskService
 * @Date 2023/4/19 16:00
 * @Author by Qin Yazhi
 */
@Service
@Slf4j
public class AsyncTaskService {

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private ApplicationContext applicationContext;

    @Async
    public void sqlLogging(ProceedingJoinPoint pjp, SqlLogging sqlLogging){
        System.err.println("sqlLogging");
        System.err.println(Thread.currentThread().getName());
        System.err.println();
        MethodSignature signature =(MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String namespace = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Configuration configuration = sqlSessionFactory.getConfiguration();
        System.err.println();
    }
}
