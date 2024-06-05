package code.sibyl.config;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;


@Component
//@DependsOn("initializer")//先运行一遍创建表
@Slf4j
public class R2dbcRoutingConfig extends AbstractRoutingConnectionFactory {

    @Value("${spring.r2dbc.url}")
    public String url;//= "r2dbc:h2:file:///./kotlin-runner/h2/sibyl;AUTO_SERVER=TRUE;MODE=LEGACY;USER=sibyl;PASSWORD=sibyl-h2-2023";

    public final static String DB_KEY = "my_r2dbc_content_key";
    private final static String defaultConnectionFactoryKey = "default";
    private Map<String, ConnectionFactory> connectionFactoryMap;

    public static <T> Mono<T> putR2dbcSource(Mono<T> mono, String group) {
        return mono.contextWrite(ctx -> ctx.put(DB_KEY, group));
    }

    public static <T> Flux<T> putR2dbcSource(Flux<T> flux, String group) {
        return flux.contextWrite(ctx -> ctx.put(DB_KEY, group));
    }

    public Mono<Map<String, ConnectionFactory>> connectionFactoryMap() {
        return Mono.just(this.connectionFactoryMap);
    }

    public Flux<ConnectionFactory> connectionFactories() {
        return Flux.fromStream(this.connectionFactoryMap.entrySet().stream().map(e -> e.getValue()));
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
        Map<String, ConnectionFactory> connectionFactoryMap = r2dbcEntityTemplate.select(Database.class)
                .all()
                .flatMap(database -> dataBaseConnectionFactoryBuild(database))
                .collect(Collectors.toMap(e -> e.getT1(), e -> e.getT2()))
                .block();
        this.setDefaultTargetConnectionFactory(connectionFactory);
        this.connectionFactoryMap = connectionFactoryMap;
        this.setTargetConnectionFactories(connectionFactoryMap);
        super.afterPropertiesSet();
    }

    @NotNull
    private Mono<Tuple2<String, ConnectionFactory>> dataBaseConnectionFactoryBuild(Database database) {
        if (StringUtils.isBlank(database.getType())) {
            throw new RuntimeException("database type must not be null");
        }
        DataBaseTypeEnum type = DataBaseTypeEnum.get(database.getType());
        ConnectionFactory factory = null;
        String host = new String(r.base64Decoder().decode(database.getHost().getBytes(StandardCharsets.UTF_8)));
        String port = new String(r.base64Decoder().decode(database.getPort().getBytes(StandardCharsets.UTF_8)));
        String username = new String(r.base64Decoder().decode(database.getUsername().getBytes(StandardCharsets.UTF_8)));
        String password = new String(r.base64Decoder().decode(database.getPassword().getBytes(StandardCharsets.UTF_8)));
        switch (type) {
            case h2 -> factory = ConnectionFactories.get(builder()
                    .option(DRIVER, database.getType())
                    .option(HOST, host)
                    .option(PORT, Integer.valueOf(host))
                    .option(USER, database.getUsername())
                    .option(PASSWORD, database.getPassword())
                    .option(DATABASE, database.getDatabase()).build());
            case postgresql -> {
                factory = ConnectionFactories.get(builder()
                        .option(DRIVER, database.getType())
                        .option(HOST, host)
                        .option(PORT, Integer.valueOf(port))
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .option(DATABASE, database.getDatabase()).build());
            }
            case mysql -> factory = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                    .host(host)
                    .port(Integer.parseInt(port))
                    .username(username)
                    .password(password)
                    .database(database.getDatabase()).build());
        }
        log.info("load => {}", database);
        return Mono.zip(Mono.just(database.getName()), Mono.just(factory));
    }

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
