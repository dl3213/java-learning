package me.sibyl.config;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.interceptor.RateLimiterInterceptor;
import me.sibyl.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dyingleaf3213
 * @Classname WebMvcConfiguration
 * @Description TODO
 * @Create 2023/06/18 20:02
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor).addPathPatterns("/request/limit/guava/**");
//        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/repeat/submit");
    }
}
