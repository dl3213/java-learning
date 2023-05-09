package me.sibyl.mq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author dyingleaf3213
 * @Classname ConsumerConfig
 * @Description TODO
 * @Create 2023/05/09 21:12
 */
@Configuration
public class ConsumerConfig {

    @Resource
    private SimpleRabbitListenerContainerFactoryConfigurer configurer;
    @Resource
    private ConnectionFactory connectionFactory;

    @Bean(name = "consumer5BatchContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(){
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(containerFactory, connectionFactory);
        containerFactory.setBatchListener(true);
        containerFactory.setBatchSize(5);
        containerFactory.setReceiveTimeout(10 * 1000L);
        containerFactory.setConsumerBatchEnabled(true);
        return containerFactory;
    }
}
