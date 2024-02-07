package code.sibyl.controller.sys.user;

import code.sibyl.common.Response;
import code.sibyl.domain.SysUser;
import code.sibyl.service.SysUserService;
import code.sibyl.util.r;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("/api/v1/sys/user")
@Slf4j
public class Controller {

    private final static String prefix = "/sys/user";

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
    public Flux<SysUser> list() {
        return r.getBean(SysUserService.class).list();
    }


    @PostMapping("/add")
    @ResponseBody
    public Mono<Response> add(@RequestBody SysUser user, ServerHttpRequest request) {
//        System.err.println(user);
//        System.err.println(request.getURI().getPath());
        return r.getBean(SysUserService.class).save(user).map(Response::success);
    }
}
