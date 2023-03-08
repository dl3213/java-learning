package me.sibyl.cas.controller;


import com.alibaba.fastjson.JSONObject;
import me.sibyl.base.entity.User;
import me.sibyl.cas.service.UserLoginService;
import me.sibyl.cas.vo.request.LoginRequest;
import me.sibyl.common.response.Response;
import me.sibyl.common.response.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname LoginController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:25
 */
@RestController
public class LoginController {

    @Resource
    private UserLoginService userLoginService;

    @PostMapping("/user/login")
    public Response login(LoginRequest user){
        System.err.println(user);
        return userLoginService.login(user);
    }

    @GetMapping("/user/logout")
    public Response logout(){
        return userLoginService.logout();
    }
}
