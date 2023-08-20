package me.sibyl.controller.sys.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/api/v1/sys/user")
public class UserController {

    @GetMapping("list")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView();
        view.setViewName("dashboard");
        return view;
    }
}
