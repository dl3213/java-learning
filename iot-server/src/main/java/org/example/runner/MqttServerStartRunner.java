package org.example.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.mqtt.MqttBroker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MqttServerStartRunner implements ApplicationRunner {
    private final MqttBroker mqttBroker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttBroker.start();
    }
}
