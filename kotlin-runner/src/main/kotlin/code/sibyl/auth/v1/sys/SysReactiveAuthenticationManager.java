//package code.sibyl.auth.v1.sys;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
///**
// *
// */
////@Component // 注入之后就会被spring security 获取
//@Slf4j
////@Primary
//public class SysReactiveAuthenticationManager implements ReactiveAuthenticationManager {
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        System.err.println("sys SysReactiveAuthenticationManager");
//        System.err.println(authentication);
//        System.err.println(authentication.getName());
//        System.err.println(authentication.getPrincipal());
//        System.err.println(authentication.getDetails());
//        System.err.println(authentication);
//        return null;
//    }
//}
