package code.sibyl.controller;

import code.sibyl.common.Response;
import code.sibyl.repository.eos.EosRepository;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/noAuth")
public class NoAuthController {

    private final EosRepository eosRepository;

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
        }) .flatMap(json -> {
            // 解析JSON字符串以获取特定属性
            // 这里使用了一个简化的例子，实际中你可能会使用一个JSON解析库
            return Mono.just(JSONObject.toJSONString(json));
        }).subscribe(e -> System.err.println(e));
        return Response.success(System.currentTimeMillis());
    }

    @PostMapping("/default/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext")
    @ResponseBody
    public Mono<Response> test(){
        return eosRepository.test().collectList().map(Response::success);
    }

    /**
     * @return
     */
    @GetMapping(value = {"/tanghe/index", "/tanghe/index/"})
    public String test(ServerHttpRequest request) {
        return "redirect:https://www.tanghenmt.com";
    }
}
