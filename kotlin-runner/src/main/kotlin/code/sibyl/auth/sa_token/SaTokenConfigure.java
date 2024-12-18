package code.sibyl.auth.sa_token;

import cn.dev33.satoken.reactor.context.SaReactorHolder;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.reactor.model.SaResponseForReactor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    @Order(2)
    public SaReactorFilter systemFilter() {
        SaReactorFilter filter = new SaReactorFilter();
        return filter
                // 指定 [拦截路由]
                .addInclude("/**")    /* 拦截所有path */
                // 指定 [放行路由]
                .addExclude(
                        "/auth/login",
                        "/sign-in.html",
                        "/favicon.ico",
                        "/css/**",
                        "/js/**",
                        "/font/**",
                        "/img/**",
                        "/dist/**",
                        "/file/**",
                        "/static-file/**",
                        "/database/socket/**",
                        "/noAuth/**",
                        "/default/**",
                        "/api/external/**",
                        "/api/kotlin/**",
                        "/eos/**"
                )
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    SaRouter.match("/**", () -> StpUtil.checkLogin());
                })
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    ServerWebExchange serverWebExchange = SaReactorSyncHolder.getContext();
                    System.err.println(serverWebExchange.getRequest().getURI().toString() + "    ->     sa全局异常 ");
                    e.printStackTrace();
                    ServerHttpResponse response = serverWebExchange.getResponse();
                    return new SaResponseForReactor(response).addHeader("msg", "认证失败").redirect("/sign-in.html");

                })
                ;
    }

    @Bean
    @Order(1)
    public SaReactorFilter restFilter() {
        SaReactorFilter filter = new SaReactorFilter();
        return filter
                // 指定 [拦截路由]
                .addInclude("/api/rest")    /* 拦截所有path */
                // 指定 [放行路由]
                .addExclude(
                        "/api/rest/auth/login"
                )
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    SaRouter.match("/**", () -> StpUtil.checkLogin());
                })
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    ServerWebExchange serverWebExchange = SaReactorSyncHolder.getContext();
                    System.err.println(serverWebExchange.getRequest().getURI().toString() + "    ->     sa全局异常 ");
                    e.printStackTrace();
                    return SaResult.error(e.getMessage());
                })
                ;
    }

}
