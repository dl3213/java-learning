package code.sibyl.service;

import code.sibyl.aop.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

    private final DatabaseClient databaseClient;

    public static void main(String[] args) {
//        Mono.deferContextual(ctx -> {
//                    System.err.println(ctx);
//                    Context context_global = (Context) ctx;
//                    return Mono.just("t")
//                            .doFirst(() -> {
//                            })
//                            .transformDeferredContextual((e, c) -> {
//                                System.err.println("transformDeferredContextual11");
//                                System.err.println(c);
//                                return e;
//                            })
//                            .contextWrite(context -> context_global.put("a", 1))
//                            .map(e -> {
//                                System.err.println("map");
//                                System.err.println(e);
//                                return e;
//                            })
//                            .contextWrite(context -> context_global.put("b", 2))
//                            .doFinally(e -> {
//                                System.err.println("doFinally");
//                                System.err.println(context_global);
//                                return;
//                            });
//                })
//                .subscribe();
//        ;

        Mono.just("t").doFirst(() -> {
            System.err.println("doFirst");
        }).transformDeferredContextual((e, c) -> {
            System.err.println("transformDeferredContextual22");
            System.err.println(c);
            return e;
        }).contextWrite(context -> context.put("a", 1)).map(e -> {
            System.err.println("map");
            System.err.println(e);
            return e;
        }).contextWrite(context -> {
            return context.put("b", 2);
        }).transformDeferredContextual((e, c) -> {
            System.err.println("transformDeferredContextual11");
            System.err.println(c);
            return e;
        }).doFinally(e -> {
            System.err.println("doFinally");
            return;
        }).subscribe();
    }

    @DS("eos")
    public Flux<Map<String, Object>> test() {
        return databaseClient.sql("select * from th_crm_contract where is_del = '0'").fetch().all();
    }
}
