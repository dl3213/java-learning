package me.sibyl.controller;

import me.sibyl.common.response.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dyingleaf3213
 * @Classname IndexController
 * @Description TODO
 * @Create 2023/06/13 20:15
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String html() {
        return "index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/login_index")
    public String login() {
        return "login1";
    }

    @RequestMapping("/login")
    public Response login(Object object) {
        System.err.println(object);
        System.err.println(object.getClass());
        return Response.success();
    }
}
