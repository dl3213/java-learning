package code.sibyl.controller.database;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.SysUser;
import code.sibyl.service.DataBaseService;
import code.sibyl.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/database")
@Slf4j
@RequiredArgsConstructor
public class DataBaseController {

    private final DataBaseService dataBaseService;

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("database/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        model.addAttribute("typeList", DataBaseTypeEnum.values());
        return Mono.create(monoSink -> monoSink.success("database/add-view"));
    }

    @PostMapping("/connect/{id}")
    @ResponseBody
    public Mono<Response> connect(@PathVariable String id) {
        System.err.println(id);
        dataBaseService.connect(id);
        return r.successMono();
    }
}
