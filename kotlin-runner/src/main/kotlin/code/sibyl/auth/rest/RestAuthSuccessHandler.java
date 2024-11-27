//package code.sibyl.auth.rest;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.server.WebFilterExchange;
//import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
//import reactor.core.publisher.Mono;
//
///**
// * 登录成功 处理器
// */
////@Component // 注入之后就会被spring security 获取
//@Slf4j
//public class RestAuthSuccessHandler implements ServerAuthenticationSuccessHandler {
//    @Override
//    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
////        log.info("认证成功");
////        System.err.println(authentication.getCredentials());
////        System.err.println(authentication.getPrincipal());
////        System.err.println(authentication.getAuthorities());
//        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
//        response.setStatusCode(HttpStatusCode.valueOf(HttpStatus.OK.value()));
//        return response.setComplete();
//    }
//}
