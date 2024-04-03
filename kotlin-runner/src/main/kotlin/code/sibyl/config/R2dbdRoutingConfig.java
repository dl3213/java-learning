package code.sibyl.config;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;


@Component
@Slf4j
public class R2dbdRoutingConfig extends AbstractRoutingConnectionFactory {

    @Value("${spring.r2dbc.url}")
    public String url;//= "r2dbc:h2:file:///./kotlin-runner/h2/sibyl;AUTO_SERVER=TRUE;MODE=LEGACY;USER=sibyl;PASSWORD=sibyl-h2-2023";

    private final static String DB_KEY = "my_r2dbc_content_key";
    private final static String defaultConnectionFactoryKey = "default";
    @Autowired
    private Base64.Decoder decoder;
    private final Map<String, ConnectionFactory> map = new ConcurrentHashMap<>();

    public static <T> Mono<T> putR2dbcSource(Mono<T> mono, String group) {
        return mono.contextWrite(ctx -> {
            System.err.println("Mono putR2dbcSource");
            System.err.println(DB_KEY);
            System.err.println(group);
            return ctx.put(DB_KEY, group);
        });
    }

    public static <T> Flux<T> putR2dbcSource(Flux<T> flux, String group) {
        return flux.contextWrite(ctx -> {
            System.err.println("Flux putR2dbcSource");
            System.err.println(DB_KEY);
            System.err.println(group);
            System.err.println(map().get(group));
            return ctx.put(DB_KEY, group);
        });
    }

    public static Map map() {
        return r.getBean(R2dbdRoutingConfig.class).map;
    }


    @Override // 或者initialize
    public void afterPropertiesSet() {
        log.info("default Connection Factory = {}", url);
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
//        DatabaseClient client = DatabaseClient.create(connectionFactory);
//        List<Map<String, Object>> block = client.sql("select * from T_BASE_DATABASE")
//                .fetch()
//                .all()
//                .map(e -> {
//                    System.err.println(e);
//                    return e;
//                })
//                .collectList()
//                .block();
        R2dbcEntityTemplate r2dbcEntityTemplate = new R2dbcEntityTemplate(connectionFactory);
        System.err.println(r2dbcEntityTemplate);
        Map<String, ConnectionFactory> connectionFactoryMap = r2dbcEntityTemplate.select(Database.class).all().flatMap(database -> {
                    System.err.println(database);
                    if (StringUtils.isBlank(database.getType())) {
                        throw new RuntimeException("database type must not be null");
                    }
                    DataBaseTypeEnum type = DataBaseTypeEnum.get(database.getType());
                    ConnectionFactory factory = null;
                    switch (type) {
                        case h2 -> factory = ConnectionFactories.get(builder()
                                .option(DRIVER, database.getType())
                                .option(HOST, database.getHost())
                                .option(PORT, Integer.valueOf(database.getPort()))
                                .option(USER, database.getUsername())
                                .option(PASSWORD, database.getPassword())
                                .option(DATABASE, database.getDatabase()).build());
                        case postgresql -> factory = ConnectionFactories.get(builder()
                                .option(DRIVER, database.getType())
                                .option(HOST, database.getHost())
                                .option(PORT, Integer.valueOf(database.getPort()))
                                .option(USER, new String(decoder.decode(database.getUsername().getBytes(StandardCharsets.UTF_8))))
                                .option(PASSWORD, new String(decoder.decode(database.getPassword().getBytes(StandardCharsets.UTF_8))))
                                .option(DATABASE, database.getDatabase()).build());
                        case mysql -> factory = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                                .host(database.getHost())
                                .port(Integer.parseInt(database.getPort()))
                                .username(database.getUsername())
                                .password(database.getPassword())
                                .database(database.getDatabase()).build());
                    }
                    return Mono.zip(Mono.just(database.getName()), Mono.just(factory));
                })
                .collect(Collectors.toMap(e -> e.getT1(), e -> e.getT2()))
                .block();
        map.putAll(connectionFactoryMap);
        this.setDefaultTargetConnectionFactory(connectionFactory);
        this.setTargetConnectionFactories(connectionFactoryMap);
        super.afterPropertiesSet();
    }

//    @Override
//    protected Mono<Object> determineCurrentLookupKey() {
//        return Mono.deferContextual(Mono::just).map(ctx -> {
//            System.err.println("determineCurrentLookupKey");
//            System.err.println(DB_KEY);
//            if (ctx.hasKey(DB_KEY)) {
//                log.info("determine datasource: " + ctx.get(DB_KEY));
//                return ctx.get(DB_KEY);
//            } else {
//                return this.defaultConnectionFactoryKey;
//            }
//        });
//    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono
                .deferContextual(Mono::just)
                .filter(it -> it.hasKey(DB_KEY))
                .map(it -> it.get(DB_KEY))
                .transform(m -> m.switchIfEmpty(Mono.just(defaultConnectionFactoryKey)))
                //.transform(m -> errorIfEmpty(m, () -> new RuntimeException(String.format("ContextView does not contain the Lookup Key '%s'", DB_KEY))))
                ;
    }

    public static <R> Mono<R> errorIfEmpty(Mono<R> mono, Supplier<Throwable> throwableSupplier) {
        return mono.switchIfEmpty(Mono.defer(() -> Mono.error(throwableSupplier.get())));
    }
}
