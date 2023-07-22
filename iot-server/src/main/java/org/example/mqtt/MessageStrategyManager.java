package org.example.mqtt;

import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MessageStrategyManager {
    public Map<MqttMessageType, MessageStrategy> messageStrategyMap = new HashMap<>();

    //根据消息类型获取对应的策略类
    public MessageStrategy getMessageStrategy(MqttMessageType messageType) {
        switch (messageType) {
            case CONNECT:
                return new ConnectAckMessageStrategy();
            case PUBLISH:
                return new PublishAckMessageStrategy();
            case PUBREL:
                return new PublishCompleteMessageStrategy();
            case SUBSCRIBE:
                return new SubscribeAckMessageStrategy();
            case UNSUBSCRIBE:
                return new UnSubscribeAckMessageStrategy();
            case PINGREQ:
                return new PingMessageStrategy();
            default:
                return null;
        }
    }

    //根据消息类型获取返回消息的类型
    private static MqttMessageType getResMqttMessageType(MqttMessageType messageType) {
        switch (messageType) {
            case CONNECT:
                return MqttMessageType.CONNACK;
            case PUBLISH:
                return MqttMessageType.PUBACK;
            case PUBREL:
                return MqttMessageType.PUBLISH;
            case SUBSCRIBE:
                return MqttMessageType.SUBACK;
            case UNSUBSCRIBE:
                return MqttMessageType.UNSUBACK;
            case PINGREQ:
                return MqttMessageType.PINGRESP;
            default:
                return null;
        }
    }
}
