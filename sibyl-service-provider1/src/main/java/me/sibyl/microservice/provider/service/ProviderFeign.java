package me.sibyl.microservice.provider.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@FeignClient(value = "FEIGN-CLIENT",name = "FEIGN-CLIENT")
public interface ProviderFeign {
    @RequestMapping(value = "/provider1",method = RequestMethod.GET)
    String provider1(@PathVariable("id") Integer id);
}
