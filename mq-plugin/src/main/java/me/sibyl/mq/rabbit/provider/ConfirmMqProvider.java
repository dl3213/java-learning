package me.sibyl.mq.rabbit.provider;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.rabbit.confirm.ConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Classname MqService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 22:10
 */
@Service
@Slf4j
public class ConfirmMqProvider {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
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
