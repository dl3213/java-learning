package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.entity.User;
import me.sibyl.sevice.AppService;
import me.sibyl.sevice.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Slf4j
public class AppTestController {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @GetMapping("/test")
    public String test() {
        User u = userService.queryById("3213");
        System.err.println(u);
        appService.service();
        return String.valueOf(System.currentTimeMillis());
    }


    @GetMapping("/user/query/detail/{id}")
    public String testQuery(@PathVariable String id) {
        User u = userService.queryById(id);
        System.err.println(u);
        return String.valueOf(System.currentTimeMillis());
    }
}
