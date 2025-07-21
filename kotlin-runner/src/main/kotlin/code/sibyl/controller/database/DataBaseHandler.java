package code.sibyl.controller.database;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
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
@ConditionalOnProperty(name = "data-source.enable", havingValue = "true", matchIfMissing = false)
public class DataBaseHandler {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(r2dbcEntityTemplate.select(Query.query(Criteria.empty()), Database.class), Database.class);
    }

//    public Mono<ServerResponse> save(ServerRequest serverRequest) {
//        Mono<Database> entityStream = serverRequest.bodyToMono(Database.class).doOnNext(e -> e.setCreateTime(LocalDateTime.now()).setCreateId(r.defaultUserId()));
//        return ServerResponse.ok().contentType(APPLICATION_JSON)
//                .body(databaseRepository.saveAll(entityStream), Database.class);
//    }
//
//    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
//        //System.err.println(Long.valueOf(serverRequest.pathVariable("id")));
//        return databaseRepository.findById(Long.valueOf(serverRequest.pathVariable("id")))
//                .flatMap(user -> databaseRepository.delete(user).then(ServerResponse.ok().build()))
//                .switchIfEmpty(ServerResponse.notFound().build());
//    }
//
//    public Mono<ServerResponse> update(ServerRequest serverRequest) {
//
//
//        return serverRequest.bodyToMono(Database.class)
//                .flatMap(e -> Mono.zip(databaseRepository.findById(e.getId()), Mono.just(e)))
//                .map(zip -> {
//                    Database database = zip.getT1();
//                    Database e = zip.getT2();
//                    database.setName(e.getName());
//                    database.setType(e.getType());
//                    database.setHost(e.getHost());
//                    database.setPort(e.getPort());
//                    database.setUsername(e.getUsername());
//                    database.setPassword(e.getPassword());
//                    database.setDatabase(e.getDatabase());
//                    database.setVersion(Objects.nonNull(database.getVersion()) ? database.getVersion() + 1 : 0);
//                    return database;
//                })
//                .doOnSuccess(e -> databaseRepository.save(e).subscribe())
//                .flatMap(e -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(Response.success(e)), Response.class));
//
//    }
}
