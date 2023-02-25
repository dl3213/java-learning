package me.sibyl.mq.rabbit.provider;

import me.sibyl.mq.rabbit.delayed.DelayedQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname DelayedMqService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 22:11
 */
@Service
public class DelayedMqProvider {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String msg, Integer ttl) {
        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.delayed_exchange_name,
                DelayedQueueConfig.delayed_routing_key,
                msg,
                (message) -> {
                    message.getMessageProperties().setDelay(ttl);
                    return message;
                });
    }
}
