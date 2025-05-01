package code.sibyl.mq.rabbit;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "demo.queue";
    public static final String Exchange = "code-sibyl-mq-rabbit-exchange";

    @Autowired
    AmqpAdmin amqpAdmin;

//    @Bean
//    public Queue myQueue() {
//        return new Queue(QUEUE, false, false, false);
//    }
//
//    @Bean
//    public TopicExchange myExchange() {
//        return new TopicExchange(Exchange);
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(myQueue()).to(myExchange()).with("demo.routingKey");
//    }

    @PostConstruct
    public void init() {
        TopicExchange exchange = new TopicExchange(Exchange);
        amqpAdmin.declareExchange(exchange);
//        Queue queue = new Queue(QUEUE);
//        amqpAdmin.declareQueue(new Queue(QUEUE));
//        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("flink.cdc.*.*.*"));


        Queue t_base_file = new Queue("t_base_file");
        amqpAdmin.declareQueue(t_base_file);
        amqpAdmin.declareBinding(BindingBuilder.bind(t_base_file).to(exchange).with("flink.cdc.postgres.t_base_file.*"));

        Queue t_biz_book = new Queue("t_biz_book");
        amqpAdmin.declareQueue(t_biz_book);
        amqpAdmin.declareBinding(BindingBuilder.bind(t_biz_book).to(exchange).with("flink.cdc.postgres.t_biz_book.*"));
    }


}
