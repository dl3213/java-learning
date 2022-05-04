package me.sibyl.controller;

import me.sibyl.common.response.ResponseVO;
import me.sibyl.dao.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public String hello(){
        return "hello";
    }

    @GetMapping("/test")
    public ResponseVO test(){
        return new ResponseVO(200, "test",userMapper.selectList(null));
    }
}

