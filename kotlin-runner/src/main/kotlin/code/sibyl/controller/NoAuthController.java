package code.sibyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/noAuth")
public class NoAuthController {

    /**
     * @return
     */
    @GetMapping(value = {"/tanghe/index", "/tanghe/index/"})
    public String test(ServerHttpRequest request) {
//        System.err.println("test");
//        request.getParameterMap().entrySet().stream().map(Map.Entry::getKey).forEach(System.err::println);
        return "redirect:https://www.tanghenmt.com";
    }
}
