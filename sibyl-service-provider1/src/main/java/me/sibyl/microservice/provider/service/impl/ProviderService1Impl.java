package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import me.sibyl.microservice.provider.service.ProviderService1;
import org.springframework.stereotype.Component;

/**
 * @Classname ProviderFeignImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:16
 */
@Component
public class ProviderService1Impl implements ProviderService1 {

    @Override
    public ResponseVO test1(RequestVO requestVO) {
        System.err.println("test1");
        return new ResponseVO(200,"from provider1",requestVO);
    }
}
