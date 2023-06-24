package me.sibyl.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dyingleaf3213
 * @Classname DataSourceConfig
 * @Description TODO
 * @Create 2023/04/09 01:24
 */

@Configuration
@ConditionalOnProperty(prefix = "datasource", name = "type", havingValue = "rw")
public class DynamicDataSourceConfig {


    @Bean(name = "master")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource master() {
        return DataSourceBuilder
                .create()
//                .url("jdbc:mysql://localhost:3306/sibyl?useSSL=false&useUnicode=true&autoReconnect=true&serverTimezone=Asia/Shanghai")
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .username("root")
//                .password("123456")
//                .type(com.alibaba.druid.pool.DruidDataSource.class)
                .build();
    }
    @Bean(name = "slave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slave() {
        return DataSourceBuilder
                .create()
//                .url("jdbc:mysql://localhost:3306/sibyl?useSSL=false&useUnicode=true&autoReconnect=true&serverTimezone=Asia/Shanghai")
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .username("root")
//                .password("123456")
//                .type(com.alibaba.druid.pool.DruidDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("master") DataSource master,
                                        @Qualifier("slave") DataSource slave) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DynamicDataSourceHolder.DB_MASTER, master);
        targetDataSource.put(DynamicDataSourceHolder.DB_SLAVE, slave);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(slave);
        return dataSource;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    public class DynamicDataSource extends AbstractRoutingDataSource {
        @Nullable
        @Override
        protected Object determineCurrentLookupKey() {
            return DynamicDataSourceHolder.getDbType();
        }
    }
}