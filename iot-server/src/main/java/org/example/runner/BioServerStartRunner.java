package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.bio.BioServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(BioServer.class)
//@DependsOn
public class BioServerStartRunner implements ApplicationRunner {
    private final BioServer server;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}
