package code.sibyl.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

public class EmptyServerWebExchange implements ServerWebExchange {
    @Override
    public ServerHttpRequest getRequest() {
        return null;
    }

    @Override
    public ServerHttpResponse getResponse() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Mono<WebSession> getSession() {
        return null;
    }

    @Override
    public <T extends Principal> Mono<T> getPrincipal() {
        return null;
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return null;
    }

    @Override
    public Mono<MultiValueMap<String, Part>> getMultipartData() {
        return null;
    }

    @Override
    public LocaleContext getLocaleContext() {
        return null;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return null;
    }

    @Override
    public boolean isNotModified() {
        return false;
    }

    @Override
    public boolean checkNotModified(Instant lastModified) {
        return false;
    }

    @Override
    public boolean checkNotModified(String etag) {
        return false;
    }

    @Override
    public boolean checkNotModified(String etag, Instant lastModified) {
        return false;
    }

    @Override
    public String transformUrl(String url) {
        return null;
    }

    @Override
    public void addUrlTransformer(Function<String, String> transformer) {

    }

    @Override
    public String getLogPrefix() {
        return null;
    }
}
