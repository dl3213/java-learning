package code.sibyl.controller;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.config.R2dbcRoutingConfig;
import code.sibyl.dto.request.EosIndexRequest;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/eos")
public class EosController {

    /**
     * todo 考虑到租赁系统人员和四区域+安翔并没有直接的关系，目前先暂行修改为显示全部数据
     */
    @PostMapping("/queryHomeRent")
    @ResponseBody
    public Mono<Response> queryHomeRent(ServerWebExchange exchange, @RequestBody EosIndexRequest indexRequest) {
        r.getBean(R2dbcRoutingConfig.class)
                .connectionFactoryMap()
                .map(e -> DatabaseClient.create(e.get("thlease_db")))
                .flatMapMany(client -> {
                    System.err.println(client);
                    return client.sql("SELECT sum(a.weighing_weight) as rentAogWeight\n" +
                            "FROM th_war_rent_aog a\n" +
                            "where is_del = '0'\n" +
                            "  and TO_DAYS(document_date) = TO_DAYS(now())").fetch().all();
                })
                .map(e -> {
                    System.err.println(e);
                    return e;
                }).subscribe()
        ;
        return r.successMono();
    }
}
