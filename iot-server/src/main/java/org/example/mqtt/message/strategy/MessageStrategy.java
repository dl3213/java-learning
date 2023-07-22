package org.example.mqtt.message.strategy;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

public interface MessageStrategy {
    void sendResponseMessage(Channel channel, MqttMessage mqttMessage);
}
