package me.sibyl.controller;

import me.sibyl.annotation.NoRepeatBeforeSubmit;
import me.sibyl.annotation.NoRepeatFilter;
import me.sibyl.common.response.Response;
import me.sibyl.vo.AppRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyingleaf3213
 * @Classname RequestController
 * @Description TODO
 * @Create 2023/06/20 21:08
 */
@RestController
@RequestMapping("/request/test")
public class RequestController {

    @PostMapping("/normal")
    @NoRepeatFilter
    public Response normal(AppRequest request) {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());
        System.err.println(request);
        return Response.success(System.currentTimeMillis());
    }

    @PostMapping("/json")
    @NoRepeatFilter
    public Response json(@RequestBody AppRequest request) {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());
        System.err.println(request);
        return Response.success(System.currentTimeMillis());
    }
}
