package me.sibyl.microservice.provider.controller;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import me.sibyl.microservice.provider.eureka.service.ProviderFeign;
import me.sibyl.microservice.provider.service.ServiceProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:54
 */
@RestController
public class AppController {


    @Resource
    private ServiceProvider serviceProvider;
    @Resource
    private ProviderFeign providerFeign;

    @GetMapping("/test")
    public String test(){
        ResponseVO test = providerFeign.test(new RequestVO());
        System.err.println(test);
        return "test2";
    }
}
