package code.sibyl.controller;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.dto.request.EosIndexRequest;
import code.sibyl.repository.eos.EosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/eos")
public class EosController {

    private final EosRepository eosRepository;

    /**
     * todo 考虑到租赁系统人员和四区域+安翔并没有直接的关系，目前先暂行修改为显示全部数据
     */
    @PostMapping("/test")
    @ResponseBody
    public Mono<Response> test(ServerWebExchange exchange, @RequestBody EosIndexRequest indexRequest) {
//        r.getBean(R2dbcRoutingConfig.class)
//                .connectionFactoryMap()
//                .map(e -> DatabaseClient.create(e.get("thlease_db")))
//                .flatMapMany(client -> {
//                    System.err.println(client);
//                    return client.sql("SELECT sum(a.weighing_weight) as rentAogWeight\n" +
//                            "FROM th_war_rent_aog a\n" +
//                            "where is_del = '0'\n" +
//                            "  and TO_DAYS(document_date) = TO_DAYS(now())").fetch().all();
//                })
//                .map(e -> {
//                    System.err.println(e);
//                    return e;
//                }).subscribe()
//        ;

//        Mono<Connection> connectionMono = connectionFactory.create();
//
//        connectionMono.flatMap(connection -> Mono.from(connection.createStatement("SELECT * FROM your_table")
//                                .execute())
//                        .flatMap(result -> Mono.from(result.map((row, meta) -> row.get(0, String.class))))
//                        .doFinally(signalType -> {
//                            if (signalType.isCancel()) {
//                                // 取消订阅时关闭连接
//                                connectionMono.flatMap(Connection::close).subscribe();
//                            }
//                        }))
//                .subscribe(
//                        row -> System.out.println(row),
//                        err -> System.err.println("Query failed: " + err),
//                        () -> System.out.println("Query completed successfully")
//                );

        return r.successMono();
    }

    @PostMapping("/queryHomeRent")
    @ResponseBody
    public Mono<Response> queryHomeRent(ServerWebExchange exchange, @RequestBody EosIndexRequest indexRequest) {
        return Mono.just(indexRequest)
                .flatMap(e -> Mono.zip(eosRepository.今日发货吨重(), eosRepository.今日退货吨重(), eosRepository.自有资产(), eosRepository.转租库存()))
                .map(e -> r.success(e.getT1()));
        //return r.successMono();
    }
}
