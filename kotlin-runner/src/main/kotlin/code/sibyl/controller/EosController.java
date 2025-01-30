package code.sibyl.controller;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.dto.request.EosIndexRequest;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.repository.eos.EosRepository;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.core.io.buffer.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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


    @PostMapping("/download/test")
    public Mono<Void> download(ServerHttpResponse response, @RequestBody EosIndexRequest indexRequest) throws IOException {

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://localhost:80/eos/export");
        RequestEntity requestEntity = new StringRequestEntity(JSONObject.toJSONString(indexRequest),"application/json;charset=UTF-8","UTF-8");
        post.setRequestEntity(requestEntity);
        int statusCode = client.executeMethod(post);
        byte[] bytes = post.getResponseBody();

        response.getHeaders().set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; " +
                "filename=demo_download.xlsx");
        response.getHeaders().add("Accept-Ranges", "bytes");
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
