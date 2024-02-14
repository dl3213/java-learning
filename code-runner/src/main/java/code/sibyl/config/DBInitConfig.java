package code.sibyl.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@Slf4j
public class DBInitConfig {

    /**
     *  项目启动初始化
     * @param factory
     * @return
     */
//    @Bean
//    public ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory factory) {
//        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(factory);
//        ClassPathResource classPathResource = new ClassPathResource("/db/schema.sql");
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(classPathResource);
//        initializer.setDatabasePopulator(populator);
//        log.info("db-initializer");
//        return initializer;
//    }
}
