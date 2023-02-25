package me.sibyl.mq.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.config.ConfirmConfig;
import me.sibyl.mq.config.DelayedQueueConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Classname ProviderController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/22 21:06
 */

@RestController
@RequestMapping("/mq/provider")
@Slf4j
public class ProviderController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public void send(String msg, Integer ttl) {
        log.info("provider --------- time = {}, send = {}, ttl = {} ", LocalDateTime.now(), msg, ttl);
        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.delayed_exchange_name,
                DelayedQueueConfig.delayed_routing_key,
                msg,
                (message) -> {
                    message.getMessageProperties().setDelay(ttl);
                    return message;
                });
    }

    @GetMapping("/confirm/send")
    public void confirmSend(String msg) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(System.currentTimeMillis()));
        log.info("provider --------- time = {}, send = {} ", LocalDateTime.now(), msg);
        rabbitTemplate.convertAndSend(
                ConfirmConfig.confirm_exchange,
                ConfirmConfig.confirm_routing_key+"2",
                msg,
                correlationData
        );
    }
}
