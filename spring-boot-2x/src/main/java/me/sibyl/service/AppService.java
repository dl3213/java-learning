package me.sibyl.service;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * @Classname AppService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/05/30 21:36
 */
@Service
@Slf4j
public class AppService
//        implements InitializingBean, DisposableBean
{

//    @PostConstruct
//    public void init(){
//        System.err.println("AppService.init");
//    }

    @Resource
    private UserMapper userMapper;
    @Resource
    private AsyncService asyncService;

    @Transactional
    public void save(User user) {
//        if(user.getName().equals("String")){
//            throw new RuntimeException("test");
//        }
        int insert = userMapper.insert(user);
        System.err.println(Thread.currentThread().getName());
//        asyncService.test(user.getId());
    }

    @Async
    public void asyncTask() {
        System.err.println(Thread.currentThread().getName());
        System.err.println(Thread.currentThread().getThreadGroup().getName());
    }

    @Async
    public Future<String> asyncString() {
        System.err.println(Thread.currentThread().getName());
        System.err.println(Thread.currentThread().getThreadGroup().getName());
        return new AsyncResult<>("test");
    }

    public void retryTest() {
        throw new RuntimeException("test");
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.err.println("afterPropertiesSet");
//    }

//    @Override
//    public void destroy() throws Exception {
//        log.error("destroy");
//    }
}
