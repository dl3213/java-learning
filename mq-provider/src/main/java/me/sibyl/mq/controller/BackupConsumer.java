package me.sibyl.mq.controller;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Classname Consumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/23 21:15
 */
@Component
@Slf4j
public class BackupConsumer {

//    @RabbitListener(queues = ConfirmConfig.confirm_queue)
//    public void consumer(Message message, Channel channel) throws Exception {
//        String msg = new String(message.getBody());
//        log.info("consumer --------- time = {}, msg = {}", LocalDateTime.now(), msg);
//    }
}
