package me.sibyl.mq.listner;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Classname DeadLetterQueueConsumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/22 21:13
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void consumer(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("consumer --------- time = {}, msg = {}", LocalDateTime.now(), msg);
    }
}
