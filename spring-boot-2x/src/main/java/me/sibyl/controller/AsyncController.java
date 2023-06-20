package me.sibyl.controller;

import lombok.SneakyThrows;
import me.sibyl.common.response.Response;
import me.sibyl.service.AppService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * @author dyingleaf3213
 * @Classname AsyncController
 * @Description TODO
 * @Create 2023/06/18 12:47
 */
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private AppService appService;

    @GetMapping("test01")
    public Response test01() {
        appService.asyncTask();
        return Response.success();
    }

    @SneakyThrows
    @GetMapping("test02")
    public Response test02() {
        Future<String> future = appService.asyncString();
        String s = future.get();
        System.err.println(s);
        return Response.success();
    }
}
