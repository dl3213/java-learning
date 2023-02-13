package me.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Classname Application
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/21 20:32
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
