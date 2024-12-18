package code.sibyl.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.r2dbc.core.DatabaseClient;

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
}
