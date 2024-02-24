package code.sibyl.auth.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 *
 */
//@Component // 注入之后就会被spring security 获取
@Slf4j
public class RestAuthManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        System.err.println("rest RestReactiveAuthenticationManager");
        System.err.println(authentication);
        System.err.println(authentication.getName());
        System.err.println(authentication.getPrincipal());
        System.err.println(authentication.getDetails());
        System.err.println(authentication);
        return null;
    }
}
