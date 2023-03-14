package me.sibyl.service.impl;

import me.sibyl.dao.BusinessOrderMapper;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Classname ServiceProvider2Impl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 06:25
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private BusinessOrderMapper businessOrderMapper;

    @Override
    public String create(OrderCreateRequest orderCreateRequest) {
        BusinessOrder order = BusinessOrder
                .builder()
                .orderId(UUID.randomUUID().toString().replaceAll("-", ""))
                .amount(String.valueOf(orderCreateRequest.getAmount()))
                .orderState("0")
                .linkId(orderCreateRequest.getLinkId())
                .createId("0")
                .createTime(LocalDateTime.now())
                .build();
        this.businessOrderMapper.insert(order);
        return order.getOrderId();
    }
}
