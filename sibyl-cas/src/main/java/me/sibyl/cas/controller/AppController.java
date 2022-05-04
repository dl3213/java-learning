package me.sibyl.cas.controller;

import me.sibyl.base.entity.User;
import me.sibyl.cas.mapper.UserMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
public class AppController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/hello")
    @PreAuthorize("@exp.hasAuth('system:dept:list')")
    public String hello(){
        return "hello";
    }

    @GetMapping("test")
    public void test(){
        List<User> users = userMapper.selectList(null);
        System.err.println(users);
    }
}
