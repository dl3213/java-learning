package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.provider.service.ServiceProvider;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Classname ProviderFeignImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:16
 */
@Component
public class ServiceProviderImpl implements ServiceProvider {

//    @Resource
//    private RestTemplate restTemplate;

    @Override
    public String test() {
//        ResponseEntity<String> exchange =
//                restTemplate.exchange(
//                        "http://service-provider1/test",
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<String>() {}
//                );
//        //System.err.println(exchange.getBody());

        System.err.println("test2");
        return "test2";
    }
}
