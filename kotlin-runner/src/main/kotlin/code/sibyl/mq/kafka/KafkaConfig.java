package code.sibyl.mq.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // ================= 公共配置 =================
//    @Bean
//    public KafkaAdmin kafkaAdmin(@Value("${spring.kafka.bootstrap-servers}") String servers) {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        return new KafkaAdmin(configs);
//    }
//
//    @Bean
//    public KafkaAdmin.NewTopics createTopics() {
//        return new KafkaAdmin.NewTopics(
//                TopicBuilder.name("test-topic")
//                        .partitions(3)
//                        .replicas(1)
//                        .build()
//        );
//    }

    // ================= 生产者配置 =================
    @Bean
    public SenderOptions<String, String> producerOptions(
            @Value("${spring.kafka.bootstrap-servers}") String servers) {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.putAll(Map.of(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.ACKS_CONFIG, "all"
        ));
        return SenderOptions.create(props);
    }

//    @Bean
//    public KafkaSender<String, String> kafkaSender(SenderOptions<String, String> options) {
//        return KafkaSender.create(options);
//    }

    // ================= 消费者配置 =================
//    @Bean
//    public ReceiverOptions<String, String> consumerOptions(
//            @Value("${spring.kafka.bootstrap-servers}") String servers,
//            @Value("${spring.kafka.consumer.group-id}") String groupId) {
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        props.putAll(Map.of(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
//                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
//        ));
//
//        return ReceiverOptions.<String, String>create(props)
//                .subscription(Collections.singleton("test-topic"));
//    }

//    @Bean
//    public KafkaReceiver<String, String> kafkaReceiver(ReceiverOptions<String, String> options) {
//        return KafkaReceiver.create(options);
//    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> template(SenderOptions senderOptions) {
        return new ReactiveKafkaProducerTemplate(senderOptions);
    }
}