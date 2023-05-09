package me.sibyl.mq.rabbit.delayed;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TTLQueueConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/22 20:44
 */
@Configuration
public class DelayedQueueConfig {

    //交换机
    public static final String delayed_queue_name = "delayed.queue";
    //队列
    public static final String delayed_exchange_name = "delayed.exchange";
    public static final String delayed_routing_key = "delayed.routingKey";

    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue queue, @Qualifier("delayedExchange") CustomExchange customExchange ){
        return BindingBuilder.bind(queue).to(customExchange).with(delayed_routing_key).noargs();
    }

    @Bean
    public Queue delayedQueue(){
        return new Queue(delayed_queue_name);
    }

    @Bean
    public CustomExchange delayedExchange() {
        //1名称2类型3是否持久化4是否需要自动删除5其他参数
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type","direct");
        return new CustomExchange(delayed_exchange_name, "x-delayed-message", true, false, args);
    }
}
