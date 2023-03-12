package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 05:42
 */
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping(value = "/consume")
    public Response consume(@RequestBody AccountConsumeRequest request) {
        log.info("[account-service] now is account-service");
        String consume = accountService.consume(request);
        return Response.success(consume);
    }

}
