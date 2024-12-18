package code.sibyl.controller.auth;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.model.SaResponseForReactor;
import cn.dev33.satoken.stp.StpUtil;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.service.SysUserService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<Void> doLogin(final Model model, ServerWebExchange exchange) {
        return Mono.zip(Mono.just(exchange), exchange.getFormData(), SysUserService.getBean().me())
                .doOnNext(tuple -> {

                    System.err.println("request -> " + tuple.getT1());
                    MultiValueMap<String, String> formData = tuple.getT2();
                    System.err.println("request formData -> " + formData);
                    System.err.println("request me -> " + tuple.getT3());

                    if("admin".equals(formData.get("username").get(0)) && "admin".equals(formData.get("password").get(0))){
                        StpUtil.login(10001, true);
                        new SaResponseForReactor(tuple.getT1().getResponse()).addHeader("msg", "认证成功").redirect("/");
                    }else {
                        new SaResponseForReactor(tuple.getT1().getResponse()).addHeader("msg", "认证失败").redirect("/sign-in.html");
                    }
                })
                .then();
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout() {
        StpUtil.logout();
        ServerWebExchange serverWebExchange = SaReactorSyncHolder.getContext();
        ServerHttpResponse response = serverWebExchange.getResponse();
        new SaResponseForReactor(response).addHeader("msg", "认证失败").redirect("/sign-in.html");

    }


    @RequestMapping("isLogin")
    @ResponseBody
    public Response isLogin() {
        System.err.println(StpUtil.getLoginId());
        System.err.println(StpUtil.getTokenInfo());
        return Response.success("当前会话是否登录：" + StpUtil.isLogin());
    }
}
