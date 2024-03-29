package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatAroundSubmit;
import me.sibyl.annotation.NoRepeatBeforeSubmit;
import me.sibyl.common.response.Response;
import me.sibyl.service.BusinessOrderService;
import me.sibyl.vo.OrderCreateRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname OrderController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 20:57
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/order")
public class OrderController {

    @Resource
    private BusinessOrderService businessOrderService;

    @RequestMapping(path = "/create", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @NoRepeatBeforeSubmit
    public Response create(@Validated OrderCreateRequest orderCreateRequest){
        Long orderId = businessOrderService.createOrder(orderCreateRequest);
        return Response.success(orderId);
    }
}
