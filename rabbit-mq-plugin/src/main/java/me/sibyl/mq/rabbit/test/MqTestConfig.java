package me.sibyl.mq.rabbit.test;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyingleaf3213
 * @Classname MqTestConfig
 * @Description TODO
 * @Create 2023/05/09 21:20
 */
@Configuration
public class MqTestConfig {
    public static final String batch_queue = "batch_queue";
    public static final String batch_exchange = "batch_exchange";
    public static final String routing_key = "batch_key";

    @Bean
    public Binding batchBinding(@Qualifier("batchQueue") Queue queue, @Qualifier("batchExchange") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routing_key+".#");
    }

    @Bean
    public Queue batchQueue(){
        return QueueBuilder.durable(batch_queue).build();
    }

    @Bean
    public TopicExchange batchExchange(){
        return ExchangeBuilder
                .topicExchange(batch_exchange)
                .durable(true)
                .build();
    }
}
