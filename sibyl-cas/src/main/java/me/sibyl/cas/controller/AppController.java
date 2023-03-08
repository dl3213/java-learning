package me.sibyl.cas.controller;

import me.sibyl.base.entity.User;
import me.sibyl.cas.mapper.UserMapper;
import me.sibyl.common.response.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:54
 */
@RestController
public class AppController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/hello")
    @PreAuthorize("@exp.hasAuth('system:dept:list')")
    public Response hello(){
        return Response.success();
    }

    @GetMapping("test")
    public Response test(){
        System.err.println("test");
        System.err.println();
        return Response.success();
    }
}
