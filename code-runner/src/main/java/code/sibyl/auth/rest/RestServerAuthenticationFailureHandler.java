package code.sibyl.auth.rest;

import code.sibyl.common.Response;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

//@Component // 注入之后就会被spring security 获取,多入口登录会被同一个处理
@Slf4j
public class RestServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.info("认证失败");
        System.err.println(webFilterExchange.getExchange().getRequest().getPath().value());
        System.err.println(webFilterExchange.getExchange().getRequest().getURI().getPath());
        System.err.println(webFilterExchange.getExchange().getRequest().getHeaders());
        System.err.println(webFilterExchange.getExchange().getRequest().getHeaders().getContentType());
        Flux<DataBuffer> body = webFilterExchange.getExchange().getRequest().getBody();
        StringBuffer bodyStr = new StringBuffer();
        String content = body.collectList().map(dataBuffers -> {
            byte[] bytes = new byte[dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum()];
            int offset = 0;
            for (DataBuffer dataBuffer : dataBuffers) {
                int length = dataBuffer.readableByteCount();
                dataBuffer.read(bytes, offset, length);
                offset += length;
                DataBufferUtils.release(dataBuffer);
            }
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }).block();
        bodyStr.append(content);
        System.err.println("content => " + content);
//        System.err.println(webFilterExchange.getExchange().getPrincipal().block().getName());
        exception.printStackTrace();
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()));
        if (exception instanceof InsufficientAuthenticationException) {
            System.err.println("请先登录~~");
        } else if (exception instanceof BadCredentialsException) {
            System.err.println("用户名或密码错误");
        } else {
            System.err.println("用户认证失败，请检查后重试");
        }
        byte[] bytes = JSONObject.toJSONString(Response.error(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
