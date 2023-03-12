package me.sibyl.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import me.sibyl.microservice.provider.eureka.AccountServiceProvider;
import me.sibyl.microservice.provider.eureka.OrderServiceProvider;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;
import me.sibyl.vo.ConsumeRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @PostMapping(value = "/test")
//    @GlobalTransactional
    @Transactional
    public Response test(@RequestBody ConsumeRequest request) {
        log.info("[test] now is AppController");

        User user = userMapper.selectById(request.getUserId());
        log.info("[test] user => " + JSONObject.toJSONString(user));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setAmount(request.getAmount());
        orderCreateRequest.setLinkId(request.getUserId());
        Response orderCreate = orderServiceProvider.create(orderCreateRequest);
        log.info("[test]order create :"+JSONObject.toJSONString(orderCreate));

        //int i = 1/0;

        AccountConsumeRequest accountConsumeRequest = new AccountConsumeRequest();
        accountConsumeRequest.setUserId(request.getUserId());
        accountConsumeRequest.setAmount(request.getAmount());
        Response consume = accountServiceProvider.consume(accountConsumeRequest);
        log.info("[test]account consume :"+JSONObject.toJSONString(consume));

        //int i = 1/0;

        user.setCreateId(String.valueOf(System.currentTimeMillis()));
        userMapper.updateById(user);
        log.info("[test] updateById => " + JSONObject.toJSONString(user));

        return Response.success();
    }
}
