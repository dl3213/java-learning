package code.sibyl.controller;

import code.sibyl.dto.MenuDTO;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final static String systemName = "未命名";


    @RequestMapping({"/", "index", "main"})
    public Mono<String> index(final Model model, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
//        System.err.println("首页");
//        System.err.println(userDetails);
//        System.err.println(databaseClient);
//        System.err.println(r2dbcEntityTemplate);
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        model.addAttribute("username", userDetails.getUsername());
        List<MenuDTO> menuTree = menuTree();
        model.addAttribute("menuTree", menuTree);
        return Mono.create(monoSink -> monoSink.success("index"));
    }

    private static List<MenuDTO> menuTree() {
        return Arrays.asList(
                new MenuDTO()
                        .setCode("sys-user")
                        .setName("用户管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("用户列表").setLinkUrl("user/list-view"))),
                new MenuDTO()
                        .setCode("sys-config")
                        .setName("配置管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("配置列表").setLinkUrl("config/list-view"))),
                new MenuDTO()
                        .setCode("database")
                        .setName("数据库管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("数据库列表").setLinkUrl("database/list-view")))
        );
    }

    @GetMapping({"sys/main",})
    public Mono<String> sys_main() {
        String welcome = "welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"pages/main",})
    public Mono<String> page_main() {
        String s = "pages/main";
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/login-view"})
    public Mono<String> login_view() {
//        return "sign-in";
        String s = "login-view";
        return Mono.create(monoSink -> monoSink.success(s));
    }


    @GetMapping("/welcome")
    public Mono<String> toWelcome() {
        String welcome = "welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

}
