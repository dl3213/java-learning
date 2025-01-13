package code.sibyl.config;

import code.sibyl.config.property.SibylMysqlConnectionProperty;
import code.sibyl.config.property.SibylPostgresqlConnectionProperty;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.r2dbc.core.DatabaseClient;

import java.nio.CharBuffer;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSourceConfig {

    private final DatabaseClient databaseClient;

    @Bean
    @Primary
    public R2dbcEntityTemplate r2dbcEntityTemplate(R2dbcConverter r2dbcConverter) {
        return new R2dbcEntityTemplate(this.databaseClient, DialectResolver.getDialect(this.databaseClient.getConnectionFactory()), r2dbcConverter);
    }

//    @ConditionalOnProperty(name = "data-source", havingValue = "sibyl-mysql", matchIfMissing = false)
    @Bean("sibyl-mysql")
    public R2dbcEntityTemplate sibylMysql(SibylMysqlConnectionProperty property) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                builder()
                        .option(DRIVER, property.getDriver())
                        .option(HOST, property.getHost())
                        .option(PORT, property.getPort())
                        .option(DATABASE, property.getDatabase())
                        .option(USER, property.getUser())
                        .option(PASSWORD, CharBuffer.wrap(property.getPassword()))
                        .build()
        );
        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory);//
//        return new R2dbcEntityTemplate(connectionFactory); // 每次sql都是新的连接
        return new R2dbcEntityTemplate(new ConnectionPool(builder.build()));
    }

//    @ConditionalOnProperty(name = "data-source", havingValue = "sibyl-postgresql", matchIfMissing = false)
    @Bean("sibyl-postgresql")
    public R2dbcEntityTemplate sibylPostgresql(SibylPostgresqlConnectionProperty property) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                builder()
                        .option(DRIVER, property.getDriver())
                        .option(HOST, property.getHost())
                        .option(PORT, property.getPort())
                        .option(DATABASE, property.getDatabase())
                        .option(USER, property.getUser())
                        .option(PASSWORD, CharBuffer.wrap(property.getPassword()))
                        .build()
        );
        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory);//
//        return new R2dbcEntityTemplate(connectionFactory); // 每次sql都是新的连接
        return new R2dbcEntityTemplate(new ConnectionPool(builder.build()));
    }
}
