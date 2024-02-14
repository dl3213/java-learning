package code.sibyl.service;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Component
@Service
@RequiredArgsConstructor
public class BeanService {

    private final Map<String,ConnectionFactoryOptions> map = new HashMap<>();


    //static静态块 > 构造方法 > @PostConstruct注解 > InitializingBean接口
    @PostConstruct
    public void init() {
        System.err.println("BeanService . init ");
//        ConnectionFactory factory = ConnectionFactories.get(
//                builder()
//                        .option(DRIVER, "postgresql")
//                        .option(HOST, "xxxx")
//                        .option(PORT, 5432)
//                        .option(USER, "postgres")
//                        .option(PASSWORD, "xxxx")
//                        .option(DATABASE, "postgres")
//                        .build());
//        DatabaseClient databaseClient = DatabaseClient.create(factory);
//        databaseClient.sql("select * from ods_xcl_eos_org_organization").fetch().all().map(m -> {
//            System.err.println(m);
//            return m;
//        }).subscribe();
    }

    public long test() {
        return System.currentTimeMillis();
    }
}
