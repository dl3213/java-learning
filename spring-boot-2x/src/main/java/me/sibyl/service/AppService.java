package me.sibyl.service;

import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * @Classname AppService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/05/30 21:36
 */
@Service
public class AppService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private AsyncService asyncService;

    @Transactional
    public void save(User user){
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
    public Future<String> asyncString(){
        System.err.println(Thread.currentThread().getName());
        System.err.println(Thread.currentThread().getThreadGroup().getName());
        return new AsyncResult<>("test");
    }
}
