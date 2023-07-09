package me.sibyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dyingleaf3213
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("me.sibyl")
public class NacosConsumer {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer.class, args);
    }

}
