package me.sibyl.mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.rabbit.topic.TopicMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Classname TopicComsumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 22:45
 */
@Component
@Slf4j
public class TopicConsumer {

    @RabbitListener(queues = TopicMqConfig.topic_queue1)
    public void consumer1(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("[{}}] time = {}, msg = {}",TopicMqConfig.topic_queue1, LocalDateTime.now(), msg);
    }

    @RabbitListener(queues = TopicMqConfig.topic_queue2)
    public void consumer2(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("[{}}] time = {}, msg = {}",TopicMqConfig.topic_queue2, LocalDateTime.now(), msg);
    }

}
