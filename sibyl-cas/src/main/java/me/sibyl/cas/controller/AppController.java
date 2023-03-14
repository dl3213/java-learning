package me.sibyl.cas.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.trace("i am trace.");
        log.debug("i am debug.");
        log.info("i am info.");
        log.warn("i am warn.");
        log.error("i am error.");
        return Response.success();
    }
}
