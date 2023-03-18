package me.sibyl.scheduling;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.listener.SibylEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Classname RunningTask
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 17:34
 */
//@Component
@Slf4j
public class RunningTask {

    private Integer taskState = 1;

    @Resource
    private ApplicationContext applicationContext;

    @Scheduled(cron = "*/10 * * * * ?")
    public void doTask() {
        log.info("[RunningTask] task tun ");
//        System.err.println(applicationContext.getClass());
        applicationContext.publishEvent(new SibylEvent("sibyl push event"));
    }
}
