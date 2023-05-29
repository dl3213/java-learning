package me.sibyl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.ThreadUtil;
import me.sibyl.dao.BusinessOrderMapper;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.entity.User;
import me.sibyl.service.BusinessOrderService;
import me.sibyl.service.UserService;
import me.sibyl.vo.OrderCreateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Classname BusinessOrderServiceImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 21:10
 */
@Service
public class BusinessOrderServiceImpl extends ServiceImpl<BusinessOrderMapper, BusinessOrder> implements BusinessOrderService {

    @Override
    public Long createOrder(OrderCreateRequest orderCreateRequest) {
        ThreadUtil.sleep(3000);
        BusinessOrder order = BusinessOrder
                .builder()
                //.orderId(UUID.randomUUID().toString().replaceAll("-", ""))
                .amount(String.valueOf(orderCreateRequest.getAmount()))
                .orderState("0")
                .linkId(orderCreateRequest.getLinkId())
                .createId("0")
                .createTime(LocalDateTime.now())
                .build();
        this.getBaseMapper().insert(order);
        return order.getId();
    }
}
