package me.sibyl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author dyingleaf3213
 * @Classname SysTool
 * @Description TODO
 * @Create 2023/04/08 20:50
 */
@Configuration
public class SystemTool {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
