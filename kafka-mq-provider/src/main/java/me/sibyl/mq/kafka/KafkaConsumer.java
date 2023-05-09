package me.sibyl.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Classname KafkaConsumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 22:09
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "sibyl")
    public void consumer(String msg){
        log.info("[KafkaConsumer] get => " + msg);
    }
}
