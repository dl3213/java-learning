package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.provider.service.ProviderService2;
import org.springframework.stereotype.Component;

/**
 * @Classname ProviderFeignImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:16
 */
@Component
public class ProviderService2Impl implements ProviderService2 {

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
