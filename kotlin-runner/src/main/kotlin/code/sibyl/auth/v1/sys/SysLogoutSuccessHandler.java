//package code.sibyl.auth.v1.sys;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.server.DefaultServerRedirectStrategy;
//import org.springframework.security.web.server.ServerRedirectStrategy;
//import org.springframework.security.web.server.WebFilterExchange;
//import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
//import org.springframework.util.Assert;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//
//@Slf4j
//public class SysLogoutSuccessHandler extends RedirectServerLogoutSuccessHandler {
//    private final URI location;
//    private ServerRedirectStrategy redirectStrategy;
//    private String msg;
//
//    public SysLogoutSuccessHandler(String location, String msg) {
//        Assert.notNull(location, "location cannot be null");
//        this.location = URI.create(location);
//        this.msg = msg;
//        this.redirectStrategy = new  SysServerRedirectStrategy(msg);
//    }
//
//    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
//        return this.redirectStrategy.sendRedirect(exchange.getExchange(), this.location);
//    }
//}
