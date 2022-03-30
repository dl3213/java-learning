package me.sibyl.microservice.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

/**
 * @Classname GatewayApp
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/30 19:47
 */
@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }
}
