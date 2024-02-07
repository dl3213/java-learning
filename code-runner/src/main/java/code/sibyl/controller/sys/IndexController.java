package code.sibyl.controller.sys;

import code.sibyl.dto.MenuDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping({"/", "index", "main"})
    public Mono<String> index(final Model model) {
        model.addAttribute("systemName", "测试系统");
        model.addAttribute("title", "测试系统");
        List<MenuDTO> menuTree = menuTree();
        model.addAttribute("menuTree", menuTree);
        return Mono.create(monoSink -> monoSink.success("index"));
    }

    private static List<MenuDTO> menuTree() {
        return Arrays.asList(
                new MenuDTO()
                        .setCode("sys-user")
                        .setName("系统用户管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("用户列表").setLinkUrl("api/sys/user/list-view"))),
                new MenuDTO()
                        .setCode("database-designer")
                        .setName("数据库管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("数据库列表").setLinkUrl("api/database/list-view")))
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
