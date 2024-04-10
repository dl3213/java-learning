package code.sibyl.service;

import code.sibyl.aop.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

    private final DatabaseClient databaseClient;

    @DS("eos")
    public Flux<Map<String, Object>> test() {
        return databaseClient.sql("select * from th_crm_contract where is_del = '0'").fetch().all();
    }
}
