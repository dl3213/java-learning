package me.sibyl.mq.rabbit.confirm;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname ConfirmConfig
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/23 21:07
 */
@Configuration
public class ConfirmConfig {

    public static final String confirm_exchange = "confirm_exchange";
    public static final String confirm_queue = "confirm_queue";
    public static final String confirm_routing_key = "confirm_routing_key";

    public static final String backup_exchange = "backup_exchange";
    public static final String backup_queue = "backup_queue";
    public static final String warning_queue = "warning_queue";

    @Bean
    public Binding bindingWarning(@Qualifier("warningQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding bindingBackup(@Qualifier("backupQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(warning_queue).build();
    }
    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(backup_queue).build();
    }
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(backup_exchange);
    }

    @Bean
    public Binding binding(@Qualifier("confirmQueue") Queue queue, @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(confirm_routing_key);
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(confirm_queue).maxPriority(10).build();
    }

    @Bean
    public DirectExchange confirmExchange(){
        return ExchangeBuilder
                .directExchange(confirm_exchange)
                .durable(true)
                .alternate(backup_exchange)
                .build();
    }
}
