package me.sibyl.sevice;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.microservice.provider.nacos.DubboAccountService;
import me.sibyl.microservice.provider.nacos.DubboOrderService;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author dyingleaf3213
 * @Classname AppService
 * @Description TODO
 * @Create 2023/03/26 04:01
 */
@Service
@Slf4j
public class AppService {

    @DubboReference
    private DubboAccountService dubboAccountService;
    @DubboReference
    private DubboOrderService dubboOrderService;

    @GlobalTransactional(name = "consume-test", rollbackFor = Exception.class)
    public void service() {
        AccountConsumeRequest requestVO = new AccountConsumeRequest();
        requestVO.setUserId("dl3213");
        requestVO.setAmount(BigDecimal.ONE);
        String consume = dubboAccountService.consume(requestVO);
        System.err.println(consume);

        //int i = 1 / 0;

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAmount(requestVO.getAmount());
        orderCreateRequest.setLinkId(requestVO.getUserId());
        String orderId = dubboOrderService.create(orderCreateRequest);
        System.err.println("[test]order create = " + orderId);

        //int i = 1 / 0;
    }
}
