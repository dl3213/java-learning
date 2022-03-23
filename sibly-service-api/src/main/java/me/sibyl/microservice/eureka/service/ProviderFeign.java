package me.sibyl.microservice.eureka.service;

import me.sibyl.microservice.eureka.request.RequestVO;
import me.sibyl.microservice.eureka.response.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@FeignClient(value = "sibyl-service-provider1")
public interface ProviderFeign {
    @PostMapping(value = "/test")
    ResponseVO test(RequestVO requestVO);
}
