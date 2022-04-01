package me.sibyl.microservice.provider.controller;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import me.sibyl.microservice.provider.eureka.service.ProviderFeign;
import me.sibyl.microservice.provider.service.ServiceProvider;
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
public class AppController implements ProviderFeign {

    @Resource
    private ServiceProvider serviceProvider;

    @Override
    @PostMapping("/test")
    public ResponseVO test(@RequestBody(required = false) RequestVO requestVO){
        ResponseVO test = serviceProvider.test(requestVO);
        return test;
    }
}
