package me.sibyl.config;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author dyingleaf3213
 * @Classname RedissonConfig
 * @Description TODO
 * @Create 2023/04/05 21:10
 */
@Configuration
public class RedissonConfig {

    @Value("${remote-server.ip}")
    private String remoteServerIp;

    @Lazy
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + remoteServerIp + ":6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

    public static void main(String[] args) {
        Config config = new Config();
//        config.useSingleServer().setAddress("redis://" + remoteServerIp + ":6379").setDatabase(4);
        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<String> bloom = redissonClient.getBloomFilter("bloom");
        bloom.tryInit(1000000L, 0.01);
        bloom.add("1");
        System.err.println(bloom.contains("1"));
        System.err.println(bloom.contains("2"));
        //删除之后：1异步定时重建2计数bloomFilter
    }

}
