package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.request.RequestVO;
import me.sibyl.common.response.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@FeignClient(value = "service-provider1",path = "/app")
public interface Provider1Feign {

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    ResponseVO test1(@RequestBody(required = false) RequestVO requestVO);
}
