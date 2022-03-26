package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
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
    public ResponseVO test(RequestVO requestVO) {
        System.err.println("test1");
        return new ResponseVO();
    }
}
