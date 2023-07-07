package me.sibyl.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import me.sibyl.microservice.provider.eureka.AccountConsumeFallbackFactoryFactory;
import me.sibyl.microservice.provider.eureka.AccountServiceProvider;
import me.sibyl.microservice.provider.eureka.OrderServiceProvider;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.vo.ConsumeRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 05:05
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AppController{

    @Resource
    private OrderServiceProvider orderServiceProvider;

    @Resource
    private AccountServiceProvider accountServiceProvider;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountConsumeFallbackFactoryFactory accountConsumeFallbackFactoryFactory;

    @PostMapping(value = "/account/consume")
    public Response consume(@RequestBody ConsumeRequest request) {

        AccountConsumeRequest accountConsumeRequest = new AccountConsumeRequest();
        accountConsumeRequest.setUserId(request.getUserId());
        accountConsumeRequest.setAmount(request.getAmount());
        Response consume = accountServiceProvider.consume(accountConsumeRequest);
        log.info("[test]account consume :"+JSONObject.toJSONString(consume));
        return Response.success();
    }


    @PostMapping(value = "/test")
    @HystrixCommand(fallbackMethod = "consumeFallback")
    public Response test(@RequestBody ConsumeRequest request) {
        log.info("[test] now is AppController");
        User user = userMapper.selectById(request.getUserId());
        log.info("[test] user => " + JSONObject.toJSONString(user));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAmount(request.getAmount());
        orderCreateRequest.setLinkId(request.getUserId());
        Response orderCreate = orderServiceProvider.create(orderCreateRequest);
        log.info("[test]order create :"+JSONObject.toJSONString(orderCreate));

//        int i = 1/0;

        AccountConsumeRequest accountConsumeRequest = new AccountConsumeRequest();
        accountConsumeRequest.setUserId(request.getUserId());
        accountConsumeRequest.setAmount(request.getAmount());
        Response consume = accountServiceProvider.consume(accountConsumeRequest);
        log.info("[test]account consume :"+JSONObject.toJSONString(consume));

        //int i = 1/0;

        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        log.info("[test] updateById => " + JSONObject.toJSONString(user));

        return Response.success();
    }


    public Response consumeFallback(ConsumeRequest request, Throwable throwable) {
        throwable.printStackTrace();
        return Response.error("consumeFallback");
    }
}
