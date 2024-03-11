package code.sibyl.service;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.domain.database.Database;
import code.sibyl.repository.DatabaseRepository;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataBaseService {
    private final DatabaseRepository databaseRepository;

    public Flux<Database> list() {
        return databaseRepository.list();
    }

    public Mono<Database> findById(Long id) {
        return databaseRepository.findById(id);
    }

    public void connect(String id) {
        databaseRepository.findById(Long.valueOf(id))
                .doOnSuccess(database -> System.err.println(database))
                .map(database -> {
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
                                .option(USER, database.getUsername())
                                .option(PASSWORD, database.getPassword())
                                .option(DATABASE, database.getDatabase()).build());
                        case mysql -> factory = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                                .host(database.getHost())
                                .port(Integer.parseInt(database.getPort()))
                                .username(database.getUsername())
                                .password(database.getPassword())
                                .database(database.getDatabase()).build());
                    }
                    return factory;
                })
                .map(DatabaseClient::create)
                .map(c -> c.sql("select now()"))
                .map(c -> c.fetch().all().map(e -> {
                    System.err.println("end");
                    System.err.println(e);
                    return e;
                }).subscribe())
                .subscribe()
        ;
//                .map(c -> c.fetch().all().map(m -> {
//                    System.err.println(m);
//                    return m;
//                }).subscribe())
//                .subscribe();

//            ConnectionPoolConfiguration configuration=ConnectionPoolConfiguration.builder(factory)
//                    .initialSize(10)//启动时连接池大小，默认10
//                    .maxIdleTime(Duration.ofMillis(1000))//最长空闲时间。如果配置为负数，则连接不会因为空闲等待而被释放。默认30分钟。注意：这个配置和backgroundEvictionInterval重复用途，这个后面说明
//                    .maxSize(10)//连接池最大大小，默认10
//                    .maxAcquireTime(Duration.ofMillis(1000))//获取连接的最长时间。也就是说，sql请求过来后，会向连接池请求一个连接，当连接全忙或者需要发起新的连接的时候，请求会处于等待状态。这个值配置最长等待多久，默认没有等待超时时间，如果获取不到会一直等下去。建议配置一下，避免流被卡住
//                    .maxCreateConnectionTime(Duration.ofMillis(1000))//创建连接的超时时间，默认不会超时
//                    .acquireRetry(1)//请求连接的重试次数，默认为1
//                    .name("db-pool")//连接池名称
//                    //.validationQuery()//探活SQL
//                    //.registerJmx()//是否注册JMX
//                    .build();
//            ConnectionPool pool=new ConnectionPool(configuration);
//            Mono<Connection> connectionMono = pool.create();
//            R2dbcEntityTemplate template = new R2dbcEntityTemplate(databaseClient.getConnectionFactory());
    }
}
