package me.sibyl.controller;

import me.sibyl.dubbo.DubboAccountServiceImpl;
import me.sibyl.microservice.request.AccountConsumeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private DubboAccountServiceImpl dubboAccountService;

    @PostMapping("/consume")
    public String consume(AccountConsumeRequest request){
        String consume = dubboAccountService.consume(request);
        System.err.println(consume);
        return String.valueOf(System.currentTimeMillis());
    }
}
