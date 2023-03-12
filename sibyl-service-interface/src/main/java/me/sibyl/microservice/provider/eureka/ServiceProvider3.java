package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.response.Response;
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
@FeignClient(value = "sibyl-eureka-consumer-provider3",path = "/provider3")
public interface ServiceProvider3 {

    @RequestMapping(value = "/query3", method = {RequestMethod.GET, RequestMethod.POST})
    Response query3(@RequestBody(required = false) String requestVO);

    @RequestMapping(value = "/update3",method = {RequestMethod.GET, RequestMethod.POST})
    Response update3(@RequestBody(required = false) String requestVO);
}
