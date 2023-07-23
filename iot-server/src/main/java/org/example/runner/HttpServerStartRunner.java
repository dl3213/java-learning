package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.http.HttpServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(HttpServer.class)
public class HttpServerStartRunner implements ApplicationRunner {
    private final HttpServer server;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}
