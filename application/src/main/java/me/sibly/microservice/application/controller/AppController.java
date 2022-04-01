package me.sibly.microservice.application.controller;

import me.sibly.microservice.application.test.Main;
import me.sibly.microservice.application.test.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
public class AppController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/test")
    public Member test(){
        return Main.getRet();
    }
}

