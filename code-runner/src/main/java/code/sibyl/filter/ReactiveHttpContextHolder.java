//package code.sibyl.filter;
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//public class ReactiveHttpContextHolder {
//
//    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;
//
//    //获取当前请求对象
//    public static Mono<ServerHttpRequest> getRequest() {
//        Mono.empty().subs()
//        return Mono.subscriberContext()
//                .map(context -> context.get(Info.CONTEXT_KEY).getRequest());
//    }
//
//    //获取当前response
//    public static Mono<ServerHttpResponse> getResponse() {
//        return Mono.subscriberContext()
//                .map(context -> context.get(Info.CONTEXT_KEY).getResponse());
//    }
//
//}
