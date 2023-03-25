package me.sibyl.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author dyingleaf3213
 * @Classname SibylConfig
 * @Description TODO
 * @Create 2023/03/26 04:14
 */
@Configuration
@MapperScan("me.sibyl.dao")
@EnableScheduling
public class SibylConfig {

}
