package me.sibyl.microservice.provider.nacos;

import me.sibyl.microservice.request.OrderCreateRequest;

/**
 * @author dyingleaf3213
 * @Classname DubboOrderService
 * @Description TODO
 * @Create 2023/03/19 20:19
 */

public interface DubboOrderService {
    String create(OrderCreateRequest request);
}
