package me.sibyl.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author dyingleaf3213
 * @Classname HystrixConfig
 * @Description TODO
 * @Create 2023/03/26 05:48
 */

@Configuration
public class HystrixConfig {

    @Bean
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }

}