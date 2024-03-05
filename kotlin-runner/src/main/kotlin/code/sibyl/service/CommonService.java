package code.sibyl.service;

import code.sibyl.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CommonService {
    public Mono<ServerResponse> test() {
        return ServerResponse.ok().build();
    }
}
