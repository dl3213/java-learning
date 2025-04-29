package code.sibyl.mq.kafka;

import code.sibyl.common.r;
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

    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;

    public static KafkaService getBean() {
        return r.getBean(KafkaService.class);
    }

    @KafkaListener(topics = "kotlin-runner-postgres-kafka-dev", groupId = "webflux-group")
    public void postgres(String message) {
        System.err.println("Received Message in topic 'kotlin-runner-postgres-kafka-dev': " + message);
    }

    @KafkaListener(topics = "test-topic", groupId = "webflux-group")
    public void test(String message) {
        System.err.println("Received Message in topic 'test-topic': " + message);
    }

    public Mono<SenderResult<Void>> send(String topic, String message) {
        return kafkaTemplate.send(topic, message);
    }
}

