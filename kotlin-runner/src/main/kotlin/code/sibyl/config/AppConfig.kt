package code.sibyl.config

import code.sibyl.service.DataBaseSocket
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter


@Configuration
@EnableR2dbcRepositories("code.sibyl.repository")
//@ComponentScan("code.sibyl.*")
//@EntityScan("code.sibyl.domain.*")
class AppConfig {

    private val log = LoggerFactory.getLogger(AppConfig::class.java)

    @Bean
    fun webSocketHandlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }

    @Bean
    fun handlerMapping(socket: DataBaseSocket): HandlerMapping {
        return SimpleUrlHandlerMapping(hashMapOf("/database/socket/{id}" to socket), -1);
    }

    /**
     * db 初始化
     * @return
     */
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        val resource = ClassPathResource("/db/schema.sql")
        populator.addPopulators(ResourceDatabasePopulator(resource))
        initializer.setDatabasePopulator(populator)
        log.info("db-initializer = {} {}", connectionFactory.metadata.name, resource.path);
        return initializer
    }

    /**
     * 全局跨域配置
     * @return
     */
    @Bean
    fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsWebFilter(source)
    }
}