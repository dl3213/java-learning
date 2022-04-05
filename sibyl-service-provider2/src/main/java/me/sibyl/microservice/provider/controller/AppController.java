package me.sibyl.microservice.provider.controller;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import me.sibyl.microservice.provider.eureka.Provider1Feign;
import me.sibyl.microservice.provider.eureka.Provider2Feign;
import me.sibyl.microservice.provider.service.ProviderService2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:54
 */
@RestController
@RequestMapping("/app")
public class AppController implements Provider2Feign {


    @Resource
    private ProviderService2 providerService2;
    @Resource
    private Provider1Feign provider1Feign;

    @PostMapping("/test2")
    @Override
    public ResponseVO test2(@RequestBody(required = false) RequestVO requestVO) {
        ResponseVO test = provider1Feign.test1(requestVO);
        System.err.println(test);
        return test;
    }
}
