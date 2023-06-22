package me.sibyl.cas.controller;

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
}
