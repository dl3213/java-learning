package org.example.mqtt.message.strategy;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class PublishAckMessageStrategy implements MessageStrategy {
    @Override
    public void sendResponseMessage(Channel channel, MqttMessage mqttMessage) {
        MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;

        //解析MqttQoS
        MqttFixedHeader mqttFixedHeader = mqttPublishMessage.fixedHeader();
        MqttQoS mqttQoS = mqttFixedHeader.qosLevel();

        int packageId = mqttPublishMessage.variableHeader().packetId();

        //解析消息
        byte[] headBytes = new byte[mqttPublishMessage.payload().readableBytes()];
        mqttPublishMessage.payload().readBytes(headBytes);
        String data = new String(headBytes);
        log.info("publish data--" + data);

        switch (mqttQoS) {
            case AT_MOST_ONCE:
                //最多一次
                break;
            case AT_LEAST_ONCE:
                //至少一次
                processAtLeastOnceMsg(channel, mqttFixedHeader, packageId);
                break;
            case EXACTLY_ONCE:
                //刚好一次
                processExactlytOnceMsg(channel, mqttFixedHeader, packageId);
                break;
            default:
                break;
        }
    }

    public void processAtLeastOnceMsg(Channel channel, MqttFixedHeader mqttFixedHeader, int packageId) {
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(packageId);
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBACK, mqttFixedHeader.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeader.isRetain(), 0x02);
        MqttPubAckMessage pubAckMsg = new MqttPubAckMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
        log.info("返回消息:" + pubAckMsg.toString());

        channel.writeAndFlush(pubAckMsg);
    }

    public void processExactlytOnceMsg(Channel channel, MqttFixedHeader mqttFixedHeader, int packageId) {
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_LEAST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(packageId);
        MqttMessage pubAckMsg = new MqttMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
        log.info("返回消息:" + pubAckMsg.toString());

        channel.writeAndFlush(pubAckMsg);
    }

}
