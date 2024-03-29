package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.bio.BioServer;
import org.example.server.mqtt.MqttBroker;
import org.example.util.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(MqttBroker.class)
public class MqttServerStartRunner implements ApplicationRunner {
    private final MqttBroker server;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}
