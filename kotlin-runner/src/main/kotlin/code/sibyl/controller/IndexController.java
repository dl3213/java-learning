package code.sibyl.controller;

import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.dto.MenuDTO;
import code.sibyl.service.DataBaseService;
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
import java.util.ArrayList;
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
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        model.addAttribute("username", userDetails.getUsername());
        List<MenuDTO> menuTree = menuTree();
        model.addAttribute("menuTree", menuTree);
        return Mono.create(monoSink -> monoSink.success("/index"));
    }

    private static List<MenuDTO> menuTree() {
        return Arrays.asList(
                new MenuDTO()
                        .setCode("system")
                        .setName("系统管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("运行状态").setLinkUrl("sys/main-view"),new MenuDTO().setName("控制面板").setLinkUrl("sys/control-view"))),
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

    @GetMapping({"home",})
    public Mono<String> home() {
        String welcome = "home";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"sys/main",})
    public Mono<String> sys_main() {
        String welcome = "default/welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"pages/main",})
    public Mono<String> page_main() {
        String s = "default/pages/main";
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/login-view"})
    public Mono<String> login_view(final Model model) {
        String s = "/login-view";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping("/welcome")
    public Mono<String> toWelcome() {
        String welcome = "default/welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"/sign-in.html"})
    public Mono<String> sign_in(final Model model) {
        String s = "/sign-in";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/settings.html"})
    public Mono<String> settings(final Model model) {
        String s = "/settings";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/form-elements.html"})
    public Mono<String> form_elements(final Model model) {
        String s = "/form-elements";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical.html"})
    public Mono<String> layout_vertical(final Model model) {
        String s = "/layout-vertical";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/uptime.html"})
    public Mono<String> uptime(final Model model) {
        String s = "/uptime";
        model.addAttribute("systemName", r.systemName);
        model.addAttribute("title", r.systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/lists.html"})
    public Mono<String> lists(final Model model) {
        String s = "/lists";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tables.html"})
    public Mono<String> tables(final Model model) {
        String s = "/tables";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/emails.html"})
    public Mono<String> emails(final Model model) {
        String s = "/emails";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/database.html"})
    public Mono<String> database(final Model model) {
        String s = "/database";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/buttons.html"})
    public Mono<String> buttons(final Model model) {
        String s = "/buttons";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/msg"})
    public Mono<String> msg(final Model model) {
        String s = "/common/msg";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
}
