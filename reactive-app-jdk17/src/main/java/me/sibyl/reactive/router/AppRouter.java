package me.sibyl.reactive.router;

import lombok.AllArgsConstructor;
import me.sibyl.reactive.dao.UserDao;
import me.sibyl.reactive.domain.QueryRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author dyingleaf3213
 * @Classname AppRouter
 * @Description TODO
 * @Create 2023/04/01 22:56
 */
@AllArgsConstructor
@Configuration
public class AppRouter {

    private static final String path_prefix = "/api/v1/router/";

    private final Validator validator;

    private UserDao userDao;

    @Bean
    public RouterFunction<ServerResponse> routers() {
        var a = 20;
        return RouterFunctions.route()
                .POST(path_prefix + "create", this::create)
                .PUT(path_prefix + "update", this::create)
                .GET(path_prefix + "get", this::create)
                .DELETE(path_prefix + "delete", this::create)
                .GET(path_prefix + "user/{id}", this::queryById)
                .build();
    }

    private Mono<ServerResponse> queryById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return null;
    }

    private Mono<ServerResponse> create(ServerRequest serverRequest) {
        return AppControllerHelper.requestBody2Mono(serverRequest, validator, QueryRequest.class).map(e -> {
            System.err.println(e);
            System.err.println(e.getClass());
            if ("123".equals(e.getId())){
                throw new RuntimeException("123");
            }
            return e.setValue("1");
        })
                .flatMap(queryRequest ->
                        ServerResponse
                                .created(UriComponentsBuilder.fromHttpRequest(serverRequest.exchange().getRequest()).path("/test01").build().toUri())
                                .bodyValue(queryRequest.setValue("1"))
                );
    }

}
