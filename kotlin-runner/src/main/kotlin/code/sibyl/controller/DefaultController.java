package code.sibyl.controller;

import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.sys.User;
import code.sibyl.dto.QueryDTO;
import code.sibyl.dto.TestDTO;
import code.sibyl.repository.eos.EosRepository;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final EosRepository eosRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @GetMapping("/json")
    @ResponseBody
    public Mono<Response> json(String url) {
        return r2dbcEntityTemplate.select(User.class)
                .all()
                .collectList()
                .map(e -> r.success(e));
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

    @RequestMapping(value = "/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext", method = {RequestMethod.GET, RequestMethod.POST})
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
//                .publishOn(Schedulers.parallel())
//                .flatMap(item -> Mono.zip(Mono.just(item), eosRepository.backNum_from(item.getSales_contract(), item.getMaterial_code())))
//                .map(item -> item.getT1().setBack_num(item.getT2().toString()))
                .collectList()
                .flatMap(list -> {
                    List<String> sales_contract_list = list.stream().filter(Objects::nonNull).map(TestDTO::getSales_contract).collect(Collectors.toList());
                    List<String> material_code_list = list.stream().filter(Objects::nonNull).map(TestDTO::getMaterial_code).collect(Collectors.toList());
                    QueryDTO queryDTO = QueryDTO.builder().setContractCodeList(sales_contract_list);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("contractCodeList", sales_contract_list);
                    return Mono.zip(Mono.just(list), eosRepository.findByCustomParams(map).collectList());
                })
                .map(r -> Response.success(r));
    }

    /**
     * @return
     */
    @GetMapping(value = {"/tanghe/index", "/tanghe/index/"})
    public String test(ServerHttpRequest request) {
        return "redirect:https://www.tanghenmt.com";
    }
}
