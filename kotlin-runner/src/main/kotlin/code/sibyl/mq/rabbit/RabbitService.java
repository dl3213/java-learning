package code.sibyl.mq.rabbit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class RabbitService {

//    private final ReactiveRabbitTemplate rabbitTemplate;

//    public static RabbitService getBean() {
//        return r.getBean(RabbitService.class);
//    }

//    @RabbitListener(queues = "code-sibyl-mq-rabbit-test", returnExceptions = "true")
    public void receiveMessage(final Object object) {
        log.info("Received message from queue: {}", object);
    }
}
