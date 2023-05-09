package me.sibyl.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.rabbit.test.MqTestConfig;
import me.sibyl.mq.rabbit.topic.TopicMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname BatchConsumer
 * @Description TODO
 * @Create 2023/05/09 21:18
 */
@RabbitListener(queues = MqTestConfig.batch_queue, containerFactory = "consumer5BatchContainerFactory")
@Slf4j
@Component
public class BatchConsumer {

    @RabbitHandler
    public void onMsg(List<String> messageList) {
        System.err.println(messageList.size());
        messageList.forEach(message -> {
            String msg = new String(message);
            log.info("[{}}] time = {}, msg = {}", MqTestConfig.batch_exchange, LocalDateTime.now(), msg);
        });
    }
}
