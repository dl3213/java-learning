package code.sibyl.runner;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 系统启动准备
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class SystemRunner implements CommandLineRunner, DisposableBean {


    @Value("${runnerEnabled}")
    private boolean runnerEnabled;
    @Value("${isDev}")
    private boolean isDev;
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;


    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化工作--start");
//        System.err.println(databaseClient);
//        System.err.println(r2dbcEntityTemplate);
//        ConnectionFactory connectionFactory = databaseClient.getConnectionFactory();
//        ConnectionFactoryMetadata metadata = connectionFactory.getMetadata();
//        System.err.println(metadata.getName());
//        databaseClient.sql("select * from T_SYS_USER").fetch().all().map(m -> {
//            System.err.println(m);
//            return m;
//        }).subscribe();
        log.info("系统初始化工作--end");
    }

    @Override
    public void destroy() throws Exception {
        log.info("com.tanghe.runner.SystemRunner.destroy");
    }

}
