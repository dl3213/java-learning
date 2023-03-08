package me.sibyl.service;

import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Classname AsyncService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/05/30 21:36
 */
@Service
public class AsyncService {

    @Resource
    private UserMapper userMapper;

    @Async
    public void test(Long id) {
        this.test2(id);
    }

    @Transactional
    public void test2(Long id) {
        User user = userMapper.selectById(id);
        System.err.println("now in async.test");
        System.err.println(Thread.currentThread().getName());
        System.err.println(user);

        throw new RuntimeException("test");
    }
}
