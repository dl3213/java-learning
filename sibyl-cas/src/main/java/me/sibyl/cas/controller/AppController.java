package me.sibyl.cas.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
public class AppController {

    @GetMapping("/hello")
    @PreAuthorize("@exp.hasAuth('system:dept:list')")
    public String hello(){
        return "hello";
    }
}
