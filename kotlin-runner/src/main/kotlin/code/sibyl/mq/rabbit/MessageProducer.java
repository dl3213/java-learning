package code.sibyl.mq.rabbit;

import code.sibyl.common.r;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {
    private final Sender sender;

    public static MessageProducer getBean() {
        return r.getBean(MessageProducer.class);
    }

    public Mono<Void> sendMessage(String message) {
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(
                        RabbitMQConfig.Exchange,
                        "demo.routingKey",
                        message.getBytes()
                ))
        ).then();
    }
}
