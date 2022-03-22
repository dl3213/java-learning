package me.sibyl.microservice.provider.service.impl;

import me.sibyl.microservice.provider.service.ProviderFeign;
import org.springframework.stereotype.Component;

/**
 * @Classname ProviderFeignImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:16
 */
@Component
public class ProviderFeignImpl implements ProviderFeign {
    @Override
    public String provider1(Integer id) {
        System.err.println("provider1 => " + id);
        return String.valueOf(id);
    }
}
