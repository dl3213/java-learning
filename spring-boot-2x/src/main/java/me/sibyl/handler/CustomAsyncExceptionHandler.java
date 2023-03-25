package me.sibyl.handler;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author dyingleaf3213
 * @Classname CustomAsyncExceptionHandler
 * @Description TODO
 * @Create 2023/03/26 07:06
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.err.println("CustomAsyncExceptionHandler");
//        System.err.println("Exception message - " + throwable.getMessage());
//        System.err.println("Method name - " + method.getName());
//        for (Object param : obj) {
//            System.err.println("Parameter value - " + param);
//        }
        throwable.printStackTrace();
    }

}
