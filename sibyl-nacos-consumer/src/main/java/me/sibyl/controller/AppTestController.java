package me.sibyl.controller;

import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.entity.User;
import me.sibyl.microservice.provider.nacos.DubboAccountService;
import me.sibyl.microservice.provider.nacos.DubboOrderService;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.sevice.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AppTestController {

    @Resource
    private UserService userService;

    @DubboReference
    private DubboAccountService dubboAccountService;
    @DubboReference
    private DubboOrderService dubboOrderService;

    @GetMapping("/test")
    @GlobalTransactional(name = "consume-test", rollbackFor = Exception.class)
    public String test() {
        User u = userService.queryById("dl3213");
        System.err.println(u);

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

        return String.valueOf(System.currentTimeMillis());
    }
}
