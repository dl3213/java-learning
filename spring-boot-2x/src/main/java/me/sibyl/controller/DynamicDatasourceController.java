package me.sibyl.controller;

import me.sibyl.common.response.Response;
import me.sibyl.entity.User;
import me.sibyl.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RestController
public class DynamicDatasourceController {


    // todo MasterSlaveAutoRoutingPlugin  +  Advisor
    @Resource
    private UserService userService;

    @GetMapping("/user/detail/{id}")
    public Response detail(@PathVariable String id){
        try {
            User detail = userService.detail(id);
            return Response.success(detail);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/user/update/{id}")
    public Response update(@PathVariable String id){
        try {
            User detail = userService.detail(id);
            //detail.setUsername(String.valueOf(RandomUtils.nextDouble()));
            detail.setAge(RandomUtils.nextInt());
            userService.updateUser(detail);
            return Response.success(System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
