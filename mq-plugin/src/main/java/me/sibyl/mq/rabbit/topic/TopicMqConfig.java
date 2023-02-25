package me.sibyl.mq.rabbit.topic;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MqConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 22:10
 */
@Configuration
public class TopicMqConfig {
    public static final String topic_exchange = "topic_exchange";
    public static final String topic_queue1 = "topic_queue1#";
    public static final String topic_queue2 = "topic_queue2*";
    public static final String topic_routing_key = "sibyl.mq";


    @Bean
    public Binding topicBinding2(@Qualifier("topicQueue2") Queue queue, @Qualifier("topicExchange") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(topic_routing_key+".*");
    }

    @Bean
    public Binding topicBinding1(@Qualifier("topicQueue1") Queue queue, @Qualifier("topicExchange") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(topic_routing_key+".#");
    }

    @Bean
    public Queue topicQueue1(){
        return QueueBuilder.durable(topic_queue1).build();
    }
    @Bean
    public Queue topicQueue2(){
        return QueueBuilder.durable(topic_queue2).build();
    }

    @Bean
    public TopicExchange topicExchange(){
        return ExchangeBuilder
                .topicExchange(topic_exchange)
                .durable(true)
                .build();
    }
}
