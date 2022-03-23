package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.eureka.request.RequestVO;
import me.sibyl.microservice.eureka.response.ResponseVO;
import me.sibyl.microservice.provider.service.ServiceProvider;
import org.springframework.stereotype.Component;

/**
 * @Classname ProviderFeignImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:16
 */
@Component
public class ServiceProviderImpl implements ServiceProvider {

    @Override
    public String test() {
        System.err.println("test1");
        return "test1";
    }
}
