package code.sibyl.mq.kafka;

import code.sibyl.common.r;
import code.sibyl.service.cdc.Handler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
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

    @KafkaListener(topicPattern = "kotlin-runner-postgres-kafka-dev", groupId = "webflux-group", properties = {""})
    public void t_base_file(String message) {
        System.err.println("t_base_file Received Message in topic 'kotlin-runner-postgres-kafka-dev': " + message);
        r.getBean(Handler.class, Handler.beanNamePrev + "t_base_file").handler(message).subscribe();
        //acknowledgment.acknowledge(); // 如果spring.kafka.consumer.enable-auto-commit=true，方法参数不能包含Acknowledgment ack，否则会报错
    }

//    @KafkaListener(topicPattern = "kotlin-runner-postgres-kafka-dev", groupId = "webflux-group")
//    public void t_biz_book(String message) {
//        System.err.println("t_biz_book Received Message in topic 'kotlin-runner-postgres-kafka-dev': " + message);
//    }

}

