package me.sibyl.microservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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
public class Provider2App {

    public static void main(String[] args) {
        SpringApplication.run(Provider2App.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
