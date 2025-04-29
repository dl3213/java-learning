package code.sibyl.mq.kafka;
import code.sibyl.common.r;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

//@Service
public class KafkaProducerService {

    private final KafkaSender<String, String> sender;
    private static final String TOPIC = "test-topic";

    public static KafkaProducerService getBean(){
        return r.getBean(KafkaProducerService.class);
    }

    public KafkaProducerService(KafkaSender<String, String> sender) {
        this.sender = sender;
    }

    public Mono<Void> sendMessage(String key, String message) {
        return sender.send(
                        Mono.just(SenderRecord.create(TOPIC, null, null, key, message, null))
                )
                .then()
                .doOnSuccess(v -> System.err.println("Message sent: " + message))
                .doOnError(e -> System.err.println("Send failed: " + e.getMessage()));
    }


}
