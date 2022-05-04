package me.sibyl.cas.controller;

import me.sibyl.base.domain.ResponseVO;
import me.sibyl.base.entity.User;
import me.sibyl.cas.service.UserLoginService;
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
    public ResponseVO login(@RequestBody User user){
        return userLoginService.login(user);
    }

    @GetMapping("/user/logout")
    public ResponseVO logout(){
        return userLoginService.logout();
    }
}
