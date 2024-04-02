package code.sibyl.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;



@Component
@Slf4j
public class R2dbdRoutingConfig extends AbstractRoutingConnectionFactory {

    private static String url = "r2dbc:h2:file:///./kotlin-runner/h2/sibyl;AUTO_SERVER=TRUE;MODE=LEGACY;USER=sibyl;PASSWORD=sibyl-h2-2023";

    private final static String DB_KEY = "my_r2dbc_content_key";
    private final String defaultConnectionFactoryKey;

    public R2dbdRoutingConfig() throws SQLException {
        super();
        System.err.println("R2dbdRoutingConfig");
        log.info("default Connection Factory = {}", url);

        defaultConnectionFactoryKey = "db";
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
        DatabaseClient client = DatabaseClient.create(connectionFactory);
        List<Map<String, Object>> block = client.sql("select * from T_BASE_DATABASE")
                .fetch()
                .all()
                .collectList()
                .block();
        block.forEach(System.err::println);
        //connectionFactory.create()

        //R2dbcEntityTemplate template = new R2dbcEntityTemplate(databaseClient.getConnectionFactory());

        this.setDefaultTargetConnectionFactory(connectionFactory);
        this.setTargetConnectionFactories(Map.of("db", connectionFactory));
//        this.setDefaultTargetConnectionFactory();
    }

//    private final ConnectionFactory connectionFactory;
//    private final DatabaseRepository databaseRepository;

//    @Override
//    public void setDefaultTargetConnectionFactory(Object defaultTargetConnectionFactory) {
//        System.err.println("setDefaultTargetConnectionFactory");
//        System.err.println(defaultTargetConnectionFactory);
//        super.setDefaultTargetConnectionFactory(defaultTargetConnectionFactory);
//    }
//
//    @Override
//    public void setTargetConnectionFactories(Map<?, ?> targetConnectionFactories) {
//        System.err.println("setTargetConnectionFactories");
//        System.err.println(targetConnectionFactories);
//        super.setTargetConnectionFactories(targetConnectionFactories);
//    }

    //    public R2dbdRoutingConfig(
//            ConnectionFactory connectionFactory
////            ,DatabaseRepository databaseRepository
//    ) {
//        System.err.println("R2dbdRoutingConfig");
//        System.err.println(connectionFactory);
////        System.err.println(databaseRepository);
////        super(db1ConnectionFactory);
////        this.setTargetConnectionFactories(Map.of(
////                "db1", db1ConnectionFactory,
////                "db2", db2ConnectionFactory
////        ));
////        this.setDefaultTargetConnectionFactory(db1ConnectionFactory);
//    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.deferContextual(Mono::just).map(ctx -> {
            if (ctx.hasKey(DB_KEY)) {
                if (log.isDebugEnabled()) {
                    log.debug("determine datasource: " + ctx.get(DB_KEY));
                }
                return ctx.get(DB_KEY);
            } else {
                return this.defaultConnectionFactoryKey;
            }
        });
//        return null;
    }

}
