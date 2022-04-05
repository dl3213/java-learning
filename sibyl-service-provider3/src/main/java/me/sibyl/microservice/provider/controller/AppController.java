package me.sibyl.microservice.provider.controller;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import me.sibyl.microservice.provider.eureka.Provider1Feign;
import me.sibyl.microservice.provider.eureka.Provider2Feign;
import me.sibyl.microservice.provider.service.ServiceProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:54
 */
@RestController
@RequestMapping("/app")
public class AppController {


    @Resource
    private ServiceProvider serviceProvider;
    @Resource
    private Provider2Feign provider2Feign;

    @PostMapping("/test3")
    public ResponseVO test3(RequestVO requestVO){
        ResponseVO test2 = provider2Feign.test2(requestVO);
        System.err.println(test2);
        return test2;
    }
}
