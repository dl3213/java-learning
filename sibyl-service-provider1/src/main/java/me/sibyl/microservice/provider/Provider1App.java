package me.sibyl.microservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname Provider1App
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 21:55
 */
@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
//@EnableEurekaClient
//@EnableFeignClients
@EnableDiscoveryClient
public class Provider1App {

    public static void main(String[] args) {
        SpringApplication.run(Provider1App.class, args);
    }
}
