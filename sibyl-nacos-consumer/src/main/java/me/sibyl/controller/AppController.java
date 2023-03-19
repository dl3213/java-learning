package me.sibyl.controller;

import me.sibyl.entity.User;
import me.sibyl.microservice.provider.nacos.DubboAccountService;
import me.sibyl.microservice.request.AccountConsumeRequest;
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
public class AppController {

    @Resource
    private UserService userService;

    @DubboReference
    private DubboAccountService dubboAccountService;

    @GetMapping("/test")
    public String test(){
        User u = userService.queryById("dl3213");
        System.err.println(u);

        AccountConsumeRequest requestVO = new AccountConsumeRequest();
        requestVO.setUserId("dl3213");
        requestVO.setAmount(BigDecimal.ONE);
        String consume = dubboAccountService.consume(requestVO);
        System.err.println(consume);
        return String.valueOf(System.currentTimeMillis());
    }
}
