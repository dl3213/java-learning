package me.sibyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.entity.User;
import me.sibyl.vo.OrderCreateRequest;

import java.util.List;

public interface BusinessOrderService extends IService<BusinessOrder> {

    String createOrder(OrderCreateRequest orderCreateRequest);
}
