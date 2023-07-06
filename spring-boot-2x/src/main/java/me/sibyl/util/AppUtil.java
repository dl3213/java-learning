package me.sibyl.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static ApplicationContext applicationContext;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AppUtil.applicationContext = event.getApplicationContext();
    }
    public static Object getContextBean(String  beanName){
        return applicationContext.getBean(beanName);
    }
    public static Object getBean(String beanName){
        return AppUtil.getContextBean(beanName);
    }
}
