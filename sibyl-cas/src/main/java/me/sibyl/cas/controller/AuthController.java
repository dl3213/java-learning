package me.sibyl.cas.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyingleaf3213
 * @Classname AuthController
 * @Description TODO
 * @Create 2023/06/23 02:57
 */
@RestController
@Slf4j
public class AuthController {

    @GetMapping("/auth/no/required")
    public Response authGet(){
        return Response.success();
    }
    @GetMapping("/home")
    public Response home(){
        return Response.success();
    }
    @GetMapping("/home/test")
    public Response homeTest(){
        return Response.success();
    }
}
