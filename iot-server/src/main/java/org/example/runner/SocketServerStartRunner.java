package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.socket.SocketServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(SocketServer.class)
public class SocketServerStartRunner implements ApplicationRunner {
    private final SocketServer server;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}
