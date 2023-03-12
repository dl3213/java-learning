package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 05:42
 */
@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping(value = "/create")
    public Response create(@RequestBody OrderCreateRequest request) {
        log.info("[order-service] now is order-service");
        String data = orderService.create(request);

        //int i = 1/0;

        return Response.success(data);
    }

}
