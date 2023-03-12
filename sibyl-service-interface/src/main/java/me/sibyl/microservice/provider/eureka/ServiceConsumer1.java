package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.ServiceRequest;
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
@FeignClient(value = "sibyl-eureka-service-consumer", path = "/app")
public interface ServiceConsumer1 {

    @RequestMapping(value = "/consume1", method = {RequestMethod.POST})
    Response consume1(ServiceRequest requestVO);
}
