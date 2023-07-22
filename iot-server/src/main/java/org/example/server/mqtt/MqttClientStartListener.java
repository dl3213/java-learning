package org.example.server.mqtt;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqttClientStartListener implements ApplicationRunner {
    @Resource
    MqttBroker mqttBroker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttBroker.start();
    }
}
