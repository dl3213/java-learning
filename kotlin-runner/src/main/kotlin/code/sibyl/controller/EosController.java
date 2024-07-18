package code.sibyl.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.dto.request.EosIndexRequest;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.repository.eos.EosRepository;
import com.alibaba.excel.EasyExcel;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.*;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/eos")
public class EosController {

    private final EosRepository eosRepository;
    private final DatabaseRepository databaseRepository;

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

    @PostMapping("/export")
    public Mono<Void> export(ServerWebExchange exchange, ServerHttpResponse response, WebSession webSession, @RequestBody EosIndexRequest indexRequest) {


        return databaseRepository.findAll().collectList().flatMap(list -> {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            EasyExcel.write(outputStream, Database.class).sheet().doWrite(list); // 假设getData()返回一个List<SimpleData>数据集
            byte[] bytes = outputStream.toByteArray();

            response.getHeaders().set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; " +
                    "filename=demo.xlsx");
            response.getHeaders().add("Accept-Ranges", "bytes");
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        });


    }

    @PostMapping("/download")
    public Mono<Void> download(ServerHttpResponse serverResponse) {

        return databaseRepository.findAll().collectList().flatMap(list -> {


            serverResponse.getHeaders().set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; " +
                    "filename=demo.xlsx");
            serverResponse.getHeaders().add("Accept-Ranges", "bytes");
            DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().allocateBuffer();
            OutputStream outputStream = dataBuffer.asOutputStream();

            ExcelWriter writer = ExcelUtil.getWriter();
            writer.writeRow(list);
            writer.flush(outputStream);

            Flux<DataBuffer> dataBufferFlux = Flux.create((FluxSink<DataBuffer> emitter) -> {
                emitter.next(dataBuffer);
                emitter.complete();
            });
            return serverResponse.writeWith(dataBufferFlux);

        });


    }
}
