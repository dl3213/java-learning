package me.sibyl.microservice.provider.eureka.service;

import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@Component
@FeignClient(value = "service-provider1")
public interface ProviderFeign {
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    ResponseVO test(@RequestBody(required = false) RequestVO requestVO);
}
