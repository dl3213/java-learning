package me.sibyl.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname TTLQueueConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/22 20:44
 */
@Configuration
public class TTLQueueConfig {

    //普通交换机
    public static final String x_exchange = "X";
    //死信交换机
    public static final String Y_dead_letter_exchange = "Y";
    //普通队列
    public static final String queue_A = "QA";
    public static final String queue_B = "QB";
    //死信队列
    public static final String dead_letter_queue = "QD";

    @Bean
    public Binding a2x(@Qualifier("queueA") Queue qa, @Qualifier("xExchange") DirectExchange ne) {
        return BindingBuilder.bind(qa).to(ne).with("XA");
    }
    @Bean
    public Binding b2x(@Qualifier("queueB") Queue qb, @Qualifier("xExchange") DirectExchange ne) {
        return BindingBuilder.bind(qb).to(ne).with("XB");
    }
    @Bean
    public Binding d2d(@Qualifier("queueD") Queue dq, @Qualifier("yExchange") DirectExchange de) {
        return BindingBuilder.bind(dq).to(de).with("YD");
    }

    @Bean("queueA")
    public Queue normal_queue_A() {
        return QueueBuilder
                .durable(queue_A)
                .deadLetterExchange(Y_dead_letter_exchange)
                .deadLetterRoutingKey("YD")
                .ttl(5 * 1000)
                .build();
    }

    @Bean("queueB")
    public Queue normal_queue_B() {
        return QueueBuilder
                .durable(queue_B)
                .deadLetterExchange(Y_dead_letter_exchange)
                .deadLetterRoutingKey("YD")
                .ttl(10 * 1000)
                .build();
    }

    @Bean("queueD")
    public Queue dead_letter_queue() {
        return QueueBuilder
                .durable(dead_letter_queue)
                .build();
    }

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(x_exchange);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_dead_letter_exchange);
    }
}
