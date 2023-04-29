package me.sibyl.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyingleaf3213
 * @Classname RequestLimitController
 * @Description TODO
 * @Create 2023/04/29 22:22
 */
@RestController
@Slf4j
@RequestMapping("/request/limit")
public class RequestLimitController {

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @GetMapping("/guava/RateLimiter")
    public Response test1(){
        //limiter.acquire(i);来以阻塞的方式获取令牌
//        double acquire = rateLimiter.acquire();
//        System.err.println("acquire => " + acquire);
        //tryAcquire(int permits, long timeout, TimeUnit unit)来设置等待超时时间的方式获取令牌，如果超timeout为0，则代表非阻塞，获取不到立即返回。
        boolean tryAcquire = rateLimiter.tryAcquire();
        if(tryAcquire){
            return Response.success(System.currentTimeMillis());
        }else {
            return Response.error("500");
        }

    }
}
