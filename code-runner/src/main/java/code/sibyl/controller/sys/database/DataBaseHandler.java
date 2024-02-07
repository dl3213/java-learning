package code.sibyl.controller.sys.database;

import code.sibyl.domain.Database;
import code.sibyl.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        //System.err.println("add" + serverRequest.path());
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(databaseRepository.saveAll(serverRequest.bodyToMono(Database.class)), Database.class);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return databaseRepository.findById(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(user -> databaseRepository.delete(user).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
