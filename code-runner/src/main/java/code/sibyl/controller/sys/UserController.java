package code.sibyl.controller.sys;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
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
    public Flux<SysUser> list(ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) throws ExecutionException, InterruptedException {
//        System.err.println(request);
//        System.err.println(userDetails.getUsername());
//        System.err.println(authentication.getName());
//        System.err.println(userDetails.getAuthorities());
//        System.err.println(authentication.getAuthorities());
////        System.err.println(ReactiveSecurityContextHolder.getContext().switchIfEmpty());
////        System.err.println(ReactiveSecurityContextHolder.getContext().map(ctx -> ctx.getAuthentication()).block());
//        ReactiveSecurityContextHolder.getContext()
//                //.switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
//                .map(context -> {
//                    System.err.println("context");
//                    System.err.println(context);
//                    return context.getAuthentication().getPrincipal();
//                })
//                .cast(UserDetails.class)
//                .subscribe(s -> System.err.println(s));
        return r.getBean(SysUserService.class).list();
    }


    @PostMapping("/add")
    @ResponseBody
    public Mono<Response> add(@RequestBody SysUser user, ServerHttpRequest request) {
//        System.err.println(user);
//        System.err.println(request.getURI().getPath());
        System.err.println();
//        Mono.just(user).map(user -> {
//            user.setCreateTime(LocalDateTime.now());
//            user.setCreateId();
//        })
        return r.getBean(SysUserService.class).save(user).map(Response::success);
    }
}
