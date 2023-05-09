package me.sibyl.mq.rabbit.callback;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.rabbit.delayed.DelayedQueueConfig;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Classname MqCallback
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/23 21:22
 */
@Slf4j
@Component//1
public class MqCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource//2
    private RabbitTemplate rabbitTemplate;

    @PostConstruct//3
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //延时队列会暂存到交换机上，也会触发这里
        if (ack){
            log.info("ack : id = " + (Objects.nonNull(correlationData) ? correlationData.getId() : null));
        }else {
            log.error("nack : id = {}, cause = {}", (Objects.nonNull(correlationData) ? correlationData.getId() : null), cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        //延时队列会暂存到交换机上，也会触发这里
        if(returnedMessage.getExchange().equals(DelayedQueueConfig.delayed_exchange_name)) return;
        log.error(
                "msg : {}, return by :{}, cause: {}, routingKey:{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey()
        );
    }
}
