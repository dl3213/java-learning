package code.sibyl.controller.sys.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DataBaseRouter {
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(DataBaseHandler handler) {
//        System.err.println(handler);
        return RouterFunctions.nest(RequestPredicates.path("/api/sys/database"),
                RouterFunctions.route(RequestPredicates.GET("/list"), handler::list)
                        .andRoute(RequestPredicates.POST("/add"), handler::save)
                        .andRoute(RequestPredicates.DELETE("/{id}"), handler::delete));
    }
}
