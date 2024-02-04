package me.sibyl.controller.sys;

import me.sibyl.vo.menu.MenuDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @GetMapping({"/", "index", "main"})
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        view.addObject("systemName", "xx系统");
        view.addObject("title", "xx系统");

        List<MenuDTO> menuTree = Arrays.asList(
                new MenuDTO().setCode("user").setName("用户管理").setChildren(Arrays.asList(new MenuDTO().setName("用户列表1").setLinkUrl("/sys/user/index"),new MenuDTO().setName("用户列表2").setLinkUrl("/sys/user/index"))),
                new MenuDTO().setCode("menu").setName("菜单管理"),
                new MenuDTO().setCode("role").setName("角色管理")
        );
        view.addObject("menuTree", menuTree);
        return view;
    }

    @GetMapping({"sys/main",})
    public String sys_main() {
        return "welcome";
    }

    @GetMapping({"pages/main",})
    public String page_main() {
        return "pages/main";
    }

    @GetMapping({"/login",})
    public String index1() {
        return "loginForm";
    }
    @GetMapping({"/error",})
    public String error() {
        return "error";
    }
    @GetMapping("/welcome")
    public String toWelcome() {
        return "welcome";
    }

}
