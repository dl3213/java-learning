package code.sibyl.mq.kafka;

import code.sibyl.common.r;
import code.sibyl.service.cdc.Handler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaService {

    public static KafkaService getBean() {
        return r.getBean(KafkaService.class);
    }

    @KafkaListener(topicPattern = "kotlin-runner-postgres-kafka-dev", groupId = "webflux-group")
    public void t_base_file(String message) {
        System.err.println("t_base_file Received Message in topic 'kotlin-runner-postgres-kafka-dev': " + message);
        r.getBean(Handler.class, Handler.beanNamePrev + "t_base_file").handler(message).subscribe();
    }

//    @KafkaListener(topicPattern = "kotlin-runner-postgres-kafka-dev", groupId = "webflux-group")
//    public void t_biz_book(String message) {
//        System.err.println("t_biz_book Received Message in topic 'kotlin-runner-postgres-kafka-dev': " + message);
//    }

}

