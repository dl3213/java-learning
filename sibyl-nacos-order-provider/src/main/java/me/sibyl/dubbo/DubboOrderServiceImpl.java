package me.sibyl.dubbo;

import me.sibyl.common.response.Response;
import me.sibyl.dao.BusinessOrderMapper;
import me.sibyl.entity.BusinessOrder;
import me.sibyl.microservice.provider.nacos.DubboOrderService;
import me.sibyl.microservice.request.OrderCreateRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author dyingleaf3213
 * @Classname DubboOrderServiceImpl
 * @Description TODO
 * @Create 2023/03/20 21:39
 */
@Service
@DubboService
public class DubboOrderServiceImpl implements DubboOrderService {

    @Resource
    private BusinessOrderMapper businessOrderMapper;

    @Override
    public String create(OrderCreateRequest request) {
        BusinessOrder order = BusinessOrder
                .builder()
                .orderId(UUID.randomUUID().toString().replaceAll("-", ""))
                .amount(String.valueOf(request.getAmount()))
                .orderState("0")
                .linkId(request.getLinkId())
                .createId("0")
                .createTime(LocalDateTime.now())
                .build();
        this.businessOrderMapper.insert(order);
        return order.getOrderId();
    }
}
