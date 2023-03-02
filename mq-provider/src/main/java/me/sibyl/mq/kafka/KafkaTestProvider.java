package me.sibyl.mq.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.Properties;

/**
 * @Classname KafkaProvider
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/02 21:30
 */

public class KafkaTestProvider {

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        kafkaProducer.send(new ProducerRecord<>("sibyl", "hello, I`m kafka"), (recordMetadata, exception)->{

        });

        kafkaProducer.close();
    }
}
