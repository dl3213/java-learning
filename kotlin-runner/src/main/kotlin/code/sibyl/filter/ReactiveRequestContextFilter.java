package code.sibyl.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * todo 日志 过滤器
 */
//@Configuration
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter {

    static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // todo
        return chain.filter(exchange).contextWrite(ctx -> {
            System.err.println("ReactiveRequestContextFilter.filter");
            return ctx.put(CONTEXT_KEY, exchange);
        });
    }
}