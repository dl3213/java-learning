package me.sibyl.controller.sys;


import lombok.extern.slf4j.Slf4j;
import me.sibyl.auth.LoginRequest;
import me.sibyl.auth.LoginService;
import me.sibyl.auth.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    public Result login(LoginRequest loginRequest) {
        try {
            System.err.println(loginRequest);
            return Result.success(loginService.login(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                log.error("BadCredentialsException=====>账号或者密码错误", e.getMessage());
                return Result.error("账号或者密码错误");
            } else {
                log.error("CustomException=====>", e.getMessage());
                return Result.error(e.getMessage());
            }
        }
    }
}
