package code.sibyl.controller;

import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping("/user")
    @ResponseBody
    public Object user() {
        return SecurityContextHolder.getContext();
    }

    @RequestMapping("/userMono")
    @ResponseBody
    @ActionLog(topic = "获取用户", type = ActionType.OTHER)
    public Object userMono() {
        System.err.println(Thread.currentThread().getName());
        return ReactiveSecurityContextHolder.getContext();
    }

}
