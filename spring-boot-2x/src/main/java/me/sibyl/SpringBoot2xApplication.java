package me.sibyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Classname Application
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/21 20:32
 */
@SpringBootApplication
@MapperScan("me.sibyl.dao")
@EnableAsync
@EnableCaching
@EnableScheduling
public class SpringBoot2xApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot2xApplication.class, args);
    }
}
