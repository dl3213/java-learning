package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.bio.BioServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BioServerStartRunner implements ApplicationRunner {
    private final BioServer bioServer;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        bioServer.start();
    }
}
