package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.microservice.request.QueryRequest;
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
@FeignClient(value = "sibyl-eureka-service-order-provider", path = "/api/v1/order")
public interface OrderServiceProvider {

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    Response create(@RequestBody OrderCreateRequest request);
}
