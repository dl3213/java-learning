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
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
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
        }).flatMap(json -> {
            // 解析JSON字符串以获取特定属性
            // 这里使用了一个简化的例子，实际中你可能会使用一个JSON解析库
            return Mono.just(JSONObject.toJSONString(json));
        }).subscribe(e -> System.err.println(e));
        return Response.success(System.currentTimeMillis());
    }

    @PostMapping("/default/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext")
    @ResponseBody
    public Mono<Response> test() {
        System.err.println("/default/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext");

//        return eosRepository.sumTest().map(item -> {
//            System.err.println("return ->");
//            System.err.println(item);
//            return Response.success(item);
//        });
//        return eosRepository.backNum("SZ20240291", "3302010001").map(item -> {
//            System.err.println(item);
//            return item;
//        }).collectList().map(item -> {
//            System.err.println("return ->");
//            System.err.println(item);
//            return Response.success(item);
//        }).doOnError(t -> {
//            System.err.println("doOnError");
//            t.printStackTrace();
//        }).doOnSuccess(r -> {
//            System.err.println("doOnSuccess");
//            System.err.println(r);
//        }).doOnTerminate(() -> {
//            System.err.println("doOnTerminate");
//        });

        return eosRepository.test()
                .publishOn(Schedulers.parallel())
                .flatMap(item -> {
                    System.err.println("flatMap ");
                    System.err.println(Thread.currentThread().getName());
                    System.err.println(Thread.currentThread().getThreadGroup().getName());
                    return Mono.zip(Mono.just(item), eosRepository.backNum_from(item.getSales_contract(), item.getMaterial_code()));
                })
                .map(item -> item.getT1().setBack_num(item.getT2().toString()))
                .collectList()
                .map(Response::success);
    }

    /**
     * @return
     */
    @GetMapping(value = {"/tanghe/index", "/tanghe/index/"})
    public String test(ServerHttpRequest request) {
        return "redirect:https://www.tanghenmt.com";
    }
}
