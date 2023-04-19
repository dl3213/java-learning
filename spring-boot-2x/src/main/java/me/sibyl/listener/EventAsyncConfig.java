package me.sibyl.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * @Classname EventAsyneConfig
 * @Description EventAsyneConfig
 * @Date 2023/4/19 16:34
 * @Author by Qin Yazhi
 */
@Configuration
public class EventAsyncConfig {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    @Bean
//    public ApplicationEventMulticaster applicationEventMulticaster(){
//        //所有事件监听都是异步了
//        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
//        eventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
//        return eventMulticaster;
//    }

}
