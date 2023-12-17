package code.sibyl.reactive.controller.common.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/sys/user")
public class UserIndexController {

    @GetMapping("index")
    public String index(){
        log.info("index");
        return "/sys/user/user-index";
    }
}
