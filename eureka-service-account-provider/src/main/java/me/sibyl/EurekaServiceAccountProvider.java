package me.sibyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
//@EnableAutoDataSourceProxy
@MapperScan("me.sibyl.dao")
public class EurekaServiceAccountProvider {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceAccountProvider.class, args);
    }

}
