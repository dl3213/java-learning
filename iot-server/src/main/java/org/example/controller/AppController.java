package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
public class AppController {

    @GetMapping("test01")
    public String test01(String id){
        System.err.println(Thread.currentThread().getName());
        System.err.println(id);
        return String.valueOf(System.currentTimeMillis() + " => " + id);
    }
}
