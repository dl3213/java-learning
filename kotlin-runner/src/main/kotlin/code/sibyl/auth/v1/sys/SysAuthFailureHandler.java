//package code.sibyl.auth.v1.sys;
//
//import code.sibyl.common.Response;
//import com.alibaba.fastjson2.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.core.log.LogMessage;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.server.DefaultServerRedirectStrategy;
//import org.springframework.security.web.server.ServerRedirectStrategy;
//import org.springframework.security.web.server.WebFilterExchange;
//import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
//import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
//import org.springframework.util.Assert;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//
///**
// * 登录失败 处理器
// */
//@Slf4j
//public class SysAuthFailureHandler implements ServerAuthenticationFailureHandler {
//    private final URI location;
//    private ServerRedirectStrategy redirectStrategy;
//    private String msg;
//
//    public SysAuthFailureHandler(String location, String msg) {
//        Assert.notNull(location, "location cannot be null");
//        this.location = URI.create(location);
//        this.msg = msg;
//        this.redirectStrategy = new  SysServerRedirectStrategy(msg);
//    }
//
//    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
//        return this.redirectStrategy.sendRedirect(webFilterExchange.getExchange(), this.location);
//    }
//}
