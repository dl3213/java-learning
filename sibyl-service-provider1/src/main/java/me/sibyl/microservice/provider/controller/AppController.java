package me.sibyl.microservice.provider.controller;

import me.sibyl.common.request.RequestVO;
import me.sibyl.common.response.ResponseVO;
import me.sibyl.microservice.provider.eureka.Provider1Feign;
import me.sibyl.microservice.provider.service.ProviderService1;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class AppController implements Provider1Feign {

    @Resource
    private ProviderService1 providerService1;

    @Override
    @PostMapping("/test1")
    public ResponseVO test1(@RequestBody(required = false) RequestVO requestVO){
        ResponseVO test = providerService1.test1(requestVO);
        return test;
    }
}
