package me.sibyl.controller;

import me.sibyl.entity.User;
import me.sibyl.sevice.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1")
public class AppController {

    @Resource
    private UserService userService;

    @GetMapping("/test")
    public String test(){
        User u = userService.queryById("dl3213");
        System.err.println(u);
        return String.valueOf(System.currentTimeMillis());
    }
}
