package me.sibyl.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.microservice.provider.eureka.ServiceConsumer1;
import me.sibyl.microservice.provider.eureka.ServiceProvider2;
import me.sibyl.microservice.request.ServiceRequest;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AppController implements ServiceConsumer1 {

    @Resource
    private ServiceProvider2 serviceProvider2;

    @Override
    @PostMapping(value = "/consume1")
    public Response consume1(ServiceRequest requestVO) {
        log.info("[consumer] now is consumer1");

        Response query = serviceProvider2.query2(requestVO.getId());
        System.err.println(JSONObject.toJSONString(query));
//        Response update = serviceProvider2.update2(requestVO);
//        System.err.println(update);
        return query;
    }
}
