package me.sibyl.controller;

import me.sibyl.service.AccountService;
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
    private AccountService accountService;

    @GetMapping("/test")
    public String test(){

        return String.valueOf(System.currentTimeMillis());
    }
}
