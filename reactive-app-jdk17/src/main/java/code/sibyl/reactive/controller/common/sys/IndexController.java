package code.sibyl.reactive.controller.common.sys;

import code.sibyl.reactive.dto.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping({"/", "index", "main"})
    public String index(final Model model) {
        System.err.println("@RequestMapping({\"/\", \"index\", \"main\"})");
        model.addAttribute("systemName", "测试系统");
        model.addAttribute("title", "测试系统");
        List<Menu> menuTree = getMenuTree();
        model.addAttribute("menuTree", menuTree);
        return "index";
    }

    private static List<Menu> getMenuTree() {
        return Arrays.asList(
                new Menu()
                        .setCode("system")
                        .setName("系统管理")
                        .setChildren(Arrays.asList(new Menu().setName("系统运行状态").setLinkUrl("api/v1/sys/running/state/index"))),
                new Menu()
                        .setCode("job")
                        .setName("任务管理")
                        .setChildren(Arrays.asList(new Menu().setName("任务列表").setLinkUrl("api/v1/job/index"))),
                new Menu()
                        .setCode("kettle")
                        .setName("kettle管理")
                        .setChildren(
                                Arrays.asList(
                                        new Menu().setName("转换列表").setLinkUrl("api/v1/kettle/index")
//                                        ,
//                                        new MenuDTO().setName("定时列表").setLinkUrl("api/v1/kettle/job/index")
                                )
                        ),
                new Menu()
                        .setCode("user")
                        .setName("用户管理")
                        .setChildren(Arrays.asList(new Menu().setName("用户列表1").setLinkUrl("/sys/user/index"), new Menu().setName("用户列表2").setLinkUrl("/sys/user/index"))),
                new Menu().setCode("menu").setName("菜单管理"),
                new Menu().setCode("role").setName("角色管理")
        );
    }

    @GetMapping({"sys/main",})
    public String sys_main() {
        return "welcome";
    }

    @GetMapping({"pages/main",})
    public String page_main() {
        return "pages/main";
    }

    @GetMapping({"/login-view",})
    public String login_view() {
//        return "sign-in";
        return "login-view";
    }
    @GetMapping("/welcome")
    public String toWelcome() {
        return "welcome";
    }

}
