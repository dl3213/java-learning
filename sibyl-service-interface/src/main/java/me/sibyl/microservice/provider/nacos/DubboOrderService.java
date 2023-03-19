package me.sibyl.microservice.provider.nacos;

import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.OrderCreateRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dyingleaf3213
 * @Classname DubboOrderService
 * @Description TODO
 * @Create 2023/03/19 20:19
 */

public interface DubboOrderService {
    Response create(OrderCreateRequest request);
}
