package code.sibyl.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DBInitConfig {
    @Bean
    public ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory factory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(factory);
        ClassPathResource classPathResource = new ClassPathResource("/db/schema.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(classPathResource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
