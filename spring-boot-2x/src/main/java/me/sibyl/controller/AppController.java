package me.sibyl.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatAroundSubmit;
import me.sibyl.annotation.NoRepeatBeforeSubmit;
import me.sibyl.aspect.TargetMode;
import me.sibyl.annotation.Watching;
import me.sibyl.common.response.Response;
import me.sibyl.common.response.ResponseVO;
import me.sibyl.entity.User;
import me.sibyl.listener.SibylEvent;
import me.sibyl.service.AppService;
import me.sibyl.service.AsyncService;
import me.sibyl.vo.AppRequest;
import me.sibyl.vo.AppRequest2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.Future;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
@Slf4j
public class AppController {
    //
//    @Resource
//    private UserMapper userMapper;
    @Resource
    private AppService appService;
    @Resource
    private AsyncService asyncService;

    @Resource
    private ApplicationContext applicationContext;

    @SneakyThrows
    @PostMapping("/user/put")
    public void put(User user) {
        // 目的 == 需求 == 出发点
        //System.err.println(Thread.currentThread().getName());
        //System.err.println(user.getId());
        System.err.println(user.getId());
//        appService.save(user);//保存数据库
        System.err.println(user.getId());

        Thread.sleep(10000);
        //System.err.println(" end ");
        //System.out.printf("test");
    }

    @GetMapping("/hello")
    @NoRepeatAroundSubmit(mode = TargetMode.classParam, watchClass = {AppRequest.class, AppRequest2.class}, classParamName = {"id", "name"})
    public Response hello(@Validated AppRequest request, AppRequest2 request2) {
        // 目的 == 需求 == 出发点
//        System.err.println("hello");
        applicationContext.publishEvent(new SibylEvent("sibyl push event"));
        return Response.success(System.currentTimeMillis());
    }

    @GetMapping("/hello2")
    @NoRepeatBeforeSubmit(mode = TargetMode.watching)
    public String hello2() {
        // 目的 == 需求 == 出发点
//        System.err.println("hello2");
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/hello3")
    @NoRepeatAroundSubmit(mode = TargetMode.watching)
    public String hello3(@Watching String param, AppRequest request, @Watching AppRequest2 request2, @Watching int paramInt) {
        // 目的 == 需求 == 出发点
//        System.err.println("hello3");
        return String.valueOf(System.currentTimeMillis());
    }


    @SneakyThrows
    @GetMapping("/async")
    public Response async() {
        System.err.println(Thread.currentThread().getName() + " => " + System.currentTimeMillis());
        asyncService.voidAsync();
        Future<String> future = asyncService.stringAsync();
//        System.err.println("future.get() => " + future.get());
        System.err.println(Thread.currentThread().getName() + " => " + System.currentTimeMillis());
        return Response.success();
    }

    @GetMapping("/cache")
    @Cacheable(
            cacheNames = {"sibyl-cache"},
            key = "#root.targetClass+'-'+#root.methodName"
    )
    public Response cache() {
        System.err.println("executed...");
        return Response.success(System.currentTimeMillis());
    }


    @Resource
    private CacheManager cacheManager;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/cache/query")
    public Response cacheQuery() {
        Cache cache = cacheManager.getCache("sibyl-cache");
        System.err.println(cache);
        System.err.println(cache.getClass());
        System.err.println(redisTemplate);
        System.err.println(redisTemplate.getClass());
        System.err.println(redisTemplate.getExpire("test"));
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        caffeineCache.getNativeCache().asMap().entrySet().forEach(c ->{
            System.err.println(c.getKey());
            System.err.println(c.getValue());
        });
        return Response.success(System.currentTimeMillis());
    }

    @Resource
    private DataSource dataSource;

    @GetMapping("/datasource")
    public Response datasource() {
        System.err.println(dataSource);
        System.err.println(dataSource.getClass());
        return Response.success(System.currentTimeMillis());
    }

    @GetMapping("test")
    public Response test(){
        log.trace("i am trace.");
        log.debug("i am debug.");
        log.info("i am info.");
        log.warn("i am warn.");
        log.error("i am error.");
        return Response.success();
    }
}

