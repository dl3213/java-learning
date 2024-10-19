package code.sibyl.controller.user;

import code.sibyl.common.Response;
import code.sibyl.common.SpringUtil;
import code.sibyl.domain.sys.User;
import code.sibyl.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        return Mono.create(monoSink -> monoSink.success("default/user/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("default/user/add-view"));
    }

    @PostMapping("/list")
    @ResponseBody
    public Mono<Response> list(ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) throws ExecutionException, InterruptedException {
        return SpringUtil.getBean(SysUserService.class).list().collectList().map(Response::success);
    }


    @PostMapping("/add")
    @ResponseBody
    public Mono<Response> add(@RequestBody User user, ServerHttpRequest request, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return SpringUtil.getBean(SysUserService.class).save(user).map(Response::success);
    }
}
