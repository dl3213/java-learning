package me.sibyl.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatBeforeSubmit;
import me.sibyl.annotation.Watching;
import me.sibyl.aspect.TargetMode;
import me.sibyl.common.response.Response;
import me.sibyl.vo.AppRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/repeat/submit")
@Slf4j
@RestController
public class RepeatSubmitController {

    @GetMapping("test01")
    @NoRepeatBeforeSubmit
    public Response test01(AppRequest appRequest) {
        log.debug("{}", new Object() {
        }.getClass().getEnclosingMethod());
        log.info("{}", appRequest);
        return Response.success(System.currentTimeMillis());
    }

    @GetMapping("test02")
    @NoRepeatBeforeSubmit(mode = TargetMode.classParam, watchClass = AppRequest.class, classParamName = "id")
    public Response test02(AppRequest appRequest) {
        log.debug("{}", new Object() {
        }.getClass().getEnclosingMethod());
        log.info("{}", appRequest);
        return Response.success(System.currentTimeMillis());
    }

    @GetMapping("test03")
    @NoRepeatBeforeSubmit(mode = TargetMode.watching)
    public Response test03(AppRequest appRequest, @Watching String id) {
        log.debug("{}", new Object() {
        }.getClass().getEnclosingMethod());
        log.info("{}", appRequest);
        System.err.println(id);
        return Response.success(System.currentTimeMillis());
    }
}
