package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.ServiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@Component
@FeignClient(value = "sibyl-eureka-service-provider2")
public interface ServiceProvider2 {

    @RequestMapping(value = "/provider2/query2", method = {RequestMethod.POST})
    Response query2(@RequestParam("id") String id);

    @RequestMapping(value = "/provider2/update2",method = {RequestMethod.POST})
    Response update2(ServiceRequest request);
}
