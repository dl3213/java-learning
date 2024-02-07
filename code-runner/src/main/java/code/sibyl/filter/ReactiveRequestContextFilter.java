//package code.sibyl.filter;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//@Configuration
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
//public class ReactiveRequestContextFilter implements WebFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
////        System.err.println("new request");
//        // todo
//        return chain.filter(exchange);
//    }
//}