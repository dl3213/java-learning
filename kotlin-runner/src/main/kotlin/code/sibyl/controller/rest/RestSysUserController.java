package code.sibyl.controller.rest;

import code.sibyl.domain.user.SysUser;
import code.sibyl.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import code.sibyl.common.r;

@RestController
@RequestMapping("/api/rest/v1/sys/user")
@Slf4j
public class RestSysUserController {

    @PostMapping("/list")
    @ResponseBody
    public Flux<SysUser> list(ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return r.getBean(SysUserService.class).list();
    }
}
