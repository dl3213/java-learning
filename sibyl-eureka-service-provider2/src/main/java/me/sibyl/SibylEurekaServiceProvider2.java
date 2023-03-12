package me.sibyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("me.sibyl.dao")
public class SibylEurekaServiceProvider2 {

    public static void main(String[] args) {
        SpringApplication.run(SibylEurekaServiceProvider2.class, args);
    }

}
