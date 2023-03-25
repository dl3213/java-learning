package me.sibyl.controller;

import me.sibyl.dubbo.DubboAccountServiceImpl;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1/account")
@RefreshScope
public class AccountController {

    @Autowired
    private DubboAccountServiceImpl dubboAccountService;

    @Resource
    private AccountService accountService;

    @PostMapping("/consume")
    public String consume(AccountConsumeRequest request) {
        String consume = dubboAccountService.consume(request);
        System.err.println(consume);
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/query/{userId}")
    public String query(@PathVariable String userId) {
        System.err.println(accountService.queryByUserId(userId));
        return String.valueOf(System.currentTimeMillis());
    }
}
