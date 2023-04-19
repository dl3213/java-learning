package me.sibyl.listener;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.util.thread.ThreadUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Classname SqlLoggingListener
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 17:54
 */
@Component
@Slf4j
public class SqlLoggingListener {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Async
    @EventListener
    public void listener(SqlLoggingEvent event) {
        log.info(String.format("%s监听到事件源：%s.", SqlLoggingListener.class.getName(), event.getSource()));
        System.err.println("SqlLoggingListener");
        System.err.println(Thread.currentThread().getName() + " in listener");
        System.err.println(event.getEventThread().getName() + " pass in listener");
        System.err.println();

        System.err.println(event);
        System.err.println(event.getSource());
        System.err.println(event.getPjp());
        System.err.println(event.getEventThread());
        System.err.println();

        MethodSignature signature = (MethodSignature) event.getPjp().getSignature();
        Method method = signature.getMethod();
        String namespace = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Configuration configuration = sqlSessionFactory.getConfiguration();
        System.err.println();

//        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//        for (Annotation[] parameterAnnotation : parameterAnnotations) {
//
//        }

        System.err.println(event.getEventThread().getName());
        StackTraceElement[] stackTrace = event.getEventThread().getStackTrace();
        for (StackTraceElement traceElement : stackTrace) {
            System.err.println(traceElement.getClassName() + "." + traceElement.getMethodName());
        }

        ThreadUtil.sleep(50000);
    }
}
