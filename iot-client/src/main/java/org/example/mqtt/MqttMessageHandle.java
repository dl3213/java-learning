package org.example.mqtt;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageHandle implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

    }
}

