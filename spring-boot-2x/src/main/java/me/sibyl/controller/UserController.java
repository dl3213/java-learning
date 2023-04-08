package me.sibyl.controller;

import lombok.SneakyThrows;
import me.sibyl.common.response.Response;
import me.sibyl.entity.User;
import me.sibyl.service.AppService;
import me.sibyl.service.AsyncService;
import me.sibyl.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController("/sys/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public Response list() {
        try {
            List<User> data = userService.listAll();
            System.err.println(data.size());
            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("detail/{id}")
    public Response detail(@PathVariable String id){
        try {
            User detail = userService.detail(id);
            return Response.success(detail);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/update/{id}")
    public Response update(@PathVariable String id){
        try {
            User detail = userService.detail(id);
            //detail.setUsername(String.valueOf(RandomUtils.nextDouble()));
            detail.setAge(RandomUtils.nextInt());
            userService.updateById(detail);
            return Response.success(System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}

