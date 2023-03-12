package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.microservice.provider.eureka.ServiceProvider2;
import me.sibyl.microservice.request.ServiceRequest;
import me.sibyl.service.UserQueryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 05:42
 */
@RestController
@RequestMapping("/provider2")
@Slf4j
public class AppController{

    @Resource
    private UserQueryService userQueryService;

    @PostMapping(value = "/query2")
    public Response query2(String id) {
        log.info("[provider2] now is provider2");
        return Response.success(userQueryService.queryDetail(id));
    }

    public Response update2(ServiceRequest request) {
        return null;
    }
}
