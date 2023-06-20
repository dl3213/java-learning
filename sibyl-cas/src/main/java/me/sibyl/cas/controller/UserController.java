package me.sibyl.cas.controller;

import me.sibyl.base.entity.User;
import me.sibyl.cas.service.UserService;
import me.sibyl.common.response.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname UserController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/05 20:50
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/put")
    public Response put(User user){
        try {
            userService.put(user);
            return Response.success();
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Response list(){
        try {
            return Response.success(userService.list());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("detail/{id}")
    public Response detail(@PathVariable String id){
        try {
            return Response.success(userService.detail(id));
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
