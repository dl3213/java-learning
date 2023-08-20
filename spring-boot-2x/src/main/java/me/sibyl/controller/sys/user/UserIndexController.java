package me.sibyl.controller.sys.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/sys/user")
public class UserIndexController {

    @GetMapping("index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView();
        view.setViewName("welcome");
        return view;
    }
}
