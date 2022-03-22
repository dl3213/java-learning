package me.sibyl.microservice.provider.controller;

import me.sibyl.microservice.provider.service.ProviderFeign;
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
    private ProviderFeign providerFeign;

    @GetMapping("/test")
    public String test(){
        System.err.println("provider2 => " + "invoke");
        return providerFeign.provider1(1);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
