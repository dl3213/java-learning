package me.sibyl.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyingleaf3213
 * @Classname RedissonConfig
 * @Description TODO
 * @Create 2023/04/05 21:10
 */
@Configuration
public class RedissonConfig {

//    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}
