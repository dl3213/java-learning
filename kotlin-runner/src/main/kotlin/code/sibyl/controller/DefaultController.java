package code.sibyl.controller;

import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.sys.User;
import code.sibyl.dto.QueryDTO;
import code.sibyl.dto.TestDTO;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/default")
public class DefaultController {

    private final MessageSource messageSource;

    @GetMapping("/greeting")
    @ResponseBody
    public Mono<String> greeting(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        System.err.println(locale);
        System.err.println(messageSource.getClass());
        return Mono.just(messageSource.getMessage("greeting", null, locale));
    }

    @GetMapping(value = "/getUrl")
    @ResponseBody
    public Mono<Response> get(String url) {
        System.err.println("url => " + url);
        return r.successMono();
    }

    @ActionLog(topic = "test", type = ActionType.OTHER)
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Long> sse(ServerWebExchange exchange) {
        System.err.println("exchange");
        return Flux.interval(Duration.ofSeconds(1))
                .map(index -> System.currentTimeMillis());
    }

    @ActionLog(topic = "test", type = ActionType.OTHER)
    @GetMapping("/mono/get")
    @ResponseBody
    public Mono<Response> mono_get() {
        return Mono.just(Response.success(System.currentTimeMillis())).contextWrite(context -> context.put("test20240804", "1"));
    }

    @ActionLog(topic = "test", type = ActionType.OTHER)
    @GetMapping("/flux/get")
    @ResponseBody
    public Flux<Long> flux_get(ServerWebExchange exchange) {
//        System.err.println(exchange.getRequest().getURI().toString());
//        return Flux.deferContextual(contextView -> Flux.just(contextView))
//                .cast(Context.class)
//                .filter(e -> e.hasKey(ServerWebExchange.class) == true)
//                .map(e -> e.get(ServerWebExchange.class))
//                .map(e -> {
//                    System.err.println(e.getRequest().getURI().toString());
//                    return System.currentTimeMillis();
//                })
//                ;
//                .subscribe();
        return Flux.fromIterable(Arrays.asList(1, 23, 45, 6, 61))
                .map(index -> System.currentTimeMillis())
//                .transformDeferredContextual((e, c) -> {
//                    System.err.println("transformDeferredContextual");
//                    System.err.println(Optional.of(c.get("test20240804")).get());
//                    ;
//                    System.err.println(e);
//                    return e;
//                })
//                .contextWrite(context -> context.put("test20240804", "1"))
                ;
    }

    @GetMapping("/get")
    @ResponseBody
    public Response get(ServerWebExchange exchange) {
        System.err.println("get");
        System.err.println(exchange);
        System.err.println(exchange.getRequest().getPath());
        System.err.println(exchange.getRequest().getQueryParams());
        System.err.println(exchange.getFormData());
        System.err.println(exchange.getMultipartData());
        return Response.success(System.currentTimeMillis());
    }

    @PostMapping("/post")
    @ResponseBody
    public Response post(ServerWebExchange exchange) {
        System.err.println("post");
        System.err.println(exchange);
        System.err.println(exchange.getRequest().getPath());
        System.err.println(exchange.getRequest().getQueryParams());
        System.err.println(exchange.getFormData());
        System.err.println(exchange.getMultipartData());
        exchange.getRequest().getBody().map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            return new String(bytes, Charset.forName("UTF-8"));
        }).flatMap(json -> {
            // 解析JSON字符串以获取特定属性
            // 这里使用了一个简化的例子，实际中你可能会使用一个JSON解析库
            return Mono.just(JSONObject.toJSONString(json));
        }).subscribe(e -> System.err.println(e));
        return Response.success(System.currentTimeMillis());
    }

    /**
     * @return
     */
    @GetMapping(value = {"/tanghe/index", "/tanghe/index/"})
    public String test(ServerHttpRequest request) {
        return "redirect:https://www.tanghenmt.com";
    }
}
