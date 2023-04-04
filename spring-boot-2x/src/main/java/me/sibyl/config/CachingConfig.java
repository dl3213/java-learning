package me.sibyl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author dyingleaf3213
 * @Classname CachingConfig
 * @Description TODO
 * @Create 2023/03/26 07:27
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Primary
    @Bean(name = "localEntityCacheManager")
    public CacheManager localEntityCacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(10)  // 初始大小
                .maximumSize(100)     // 最大容量
                .recordStats()        // 打开统计
                .expireAfterAccess(10, TimeUnit.MINUTES);  // 10分钟不访问自动丢弃
//              .executor(ThreadPoolUtil.getThreadPool()); // 走线程池，需自定义线程池,可不用
        caffeineCacheManager.setCaffeine(caffeine);
        caffeineCacheManager.setCacheNames(Arrays.asList("sibyl-cache"));  // 设定缓存器名称
        caffeineCacheManager.setAllowNullValues(false);  // 值不可为空
        return caffeineCacheManager;
    }
}
