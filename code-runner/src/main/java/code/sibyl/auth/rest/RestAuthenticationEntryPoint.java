package code.sibyl.auth.rest;

import code.sibyl.common.Response;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 *  未验证 处理器
 */
@Slf4j
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        System.err.println("rest RestServerAuthenticationEntryPoint");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()));
        byte[] bytes = JSONObject.toJSONString(Response.error(HttpStatus.UNAUTHORIZED.value(), "未认证")).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
