package code.sibyl.mq.rabbit;

import code.sibyl.common.r;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.Receiver;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {
    private final Receiver receiver;

    public static MessageConsumer getBean() {
        return r.getBean(MessageConsumer.class);
    }

    @PostConstruct
    public void startConsuming() {
        System.err.println("code.sibyl.rabbit.MessageConsumer");
        receiver.consumeAutoAck("demo.queue")
                .map(delivery -> {
                    String s = new String(delivery.getBody());
                    System.err.println(s);
                    return s;
                })
                .doOnNext(msg -> log.info("code.sibyl.rabbit.MessageConsumer -> Received message: {}", msg))
                .subscribe();
    }
}
