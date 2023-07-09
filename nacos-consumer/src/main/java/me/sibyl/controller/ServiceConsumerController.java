package me.sibyl.controller;

import com.alibaba.nacos.common.utils.ThreadUtils;
import io.seata.spring.annotation.GlobalTransactional;
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

@RestController
@RequestMapping("/api/v1/consumer")
public class ServiceConsumerController {

    @DubboReference
    private DubboAccountService dubboAccountService;
    @DubboReference
    private DubboOrderService dubboOrderService;

    @Resource
    private UserService userService;

    @GetMapping("/test")
    @GlobalTransactional(name = "consume-test", rollbackFor = Exception.class)
    public String test(String userId, String amount) {

        ThreadUtils.sleep(1000);

        User u = userService.queryById("3213");
        System.err.println(u);
        Long uid = Long.valueOf(userId);
        BigDecimal bigDecimal = new BigDecimal(amount);

        AccountConsumeRequest requestVO = new AccountConsumeRequest();
        requestVO.setUserId(uid);
        requestVO.setAmount(bigDecimal);
        Long consume = dubboAccountService.consume(requestVO);
        System.err.println(consume);
        if(consume <= 0L){
            throw new RuntimeException("消费失败");
        }

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAmount(requestVO.getAmount());
        orderCreateRequest.setLinkId(requestVO.getUserId());
        Long orderId = dubboOrderService.create(orderCreateRequest);
        System.err.println("[test]order create = " + orderId);

//        int i = 1 / 0;

        return String.valueOf(System.currentTimeMillis());
    }
}
