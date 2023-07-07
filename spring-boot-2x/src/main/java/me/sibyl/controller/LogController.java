package me.sibyl.controller;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.vo.AppRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
@Slf4j
//@Log4j2
public class LogController {

    @GetMapping("test01")
    public Response test01(AppRequest request){
        System.err.println("test");
        System.err.println("test0101");
        System.err.println("test111");
        log.trace("i am trace.{}", request);
        log.debug("i am debug.");
        log.info("i am info.");
        log.warn("i am warn.");
        log.error("i am error.");
        System.err.println(log.getClass().getName());
        return Response.success();
    }
}
