package me.sibyl.mq.listner;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.config.DelayedQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Classname Consumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/19 21:44
 */
@Component
@Slf4j
public class DelayedQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.delayed_queue_name)
    public void consumer(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("consumer --------- time = {}, msg = {}", LocalDateTime.now(), msg);
    }
}
