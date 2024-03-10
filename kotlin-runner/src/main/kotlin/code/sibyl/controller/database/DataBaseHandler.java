package code.sibyl.controller.database;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataBaseHandler {

    private final DatabaseRepository databaseRepository;

    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(databaseRepository.findAll(), Database.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Database> entityStream = serverRequest.bodyToMono(Database.class).doOnNext(e -> e.setCreateTime(LocalDateTime.now()).setCreateId(0L));
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(databaseRepository.saveAll(entityStream), Database.class);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        //System.err.println(Long.valueOf(serverRequest.pathVariable("id")));
        return databaseRepository.findById(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(user -> databaseRepository.delete(user).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Response> responseMono = serverRequest.bodyToMono(Database.class).map(e -> {
            try {
                Database database = databaseRepository.findById(e.getId()).toFuture().get();
                database.setName(e.getName());
                database.setType(e.getType());
                database.setHost(e.getHost());
                database.setPort(e.getPort());
                database.setUsername(e.getUsername());
                database.setPassword(e.getPassword());
                database.setDatabase(e.getDatabase());
                database.setVersion(Objects.nonNull(database.getVersion()) ? database.getVersion() + 1 : 0);
                databaseRepository.save(database).subscribe();
                return r.success();
            } catch (Exception exception) {
                exception.printStackTrace();
                return r.error(exception.getMessage());
            }
        });
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(responseMono, Response.class);
    }
}
