package me.sibyl.config;

import me.sibyl.interceptor.RepeatableFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.io.Serializable;

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
