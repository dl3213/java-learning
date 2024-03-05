package code.sibyl.controller.sys.user;

import code.sibyl.common.Response;
import code.sibyl.domain.SysUser;
import code.sibyl.service.SysUserService;
import code.sibyl.util.r;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final static String prefix = "sys/user";

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        model.addAttribute("prefix", prefix);
        return Mono.create(monoSink -> monoSink.success(prefix + "/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        model.addAttribute("prefix", prefix);
        return Mono.create(monoSink -> monoSink.success(prefix + "/add-view"));
    }

    @PostMapping("/list")
    @ResponseBody
    public Mono<Response> list(ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) throws ExecutionException, InterruptedException {
        return r.getBean(SysUserService.class).list().collectList().map(r::success);
    }


    @PostMapping("/add")
    @ResponseBody
    public Mono<Response> add(@RequestBody SysUser user, ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return r.getBean(SysUserService.class).save(user).map(Response::success);
    }
}
