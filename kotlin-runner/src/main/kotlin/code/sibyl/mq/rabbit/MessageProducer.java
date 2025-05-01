package code.sibyl.mq.rabbit;

import code.sibyl.common.r;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public static MessageProducer getBean() {
        return r.getBean(MessageProducer.class);
    }

    public void send(String msg) {
        rabbitTemplate.convertAndSend(new Message(msg.getBytes()));
    }

    public void send(String routingKey, String msg) {
        rabbitTemplate.convertAndSend(routingKey, new Message(msg.getBytes()));
    }
}
