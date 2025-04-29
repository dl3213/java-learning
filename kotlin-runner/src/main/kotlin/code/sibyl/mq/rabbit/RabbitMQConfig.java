package code.sibyl.mq.rabbit;

import code.sibyl.common.r;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "demo.queue";
    public static final String Exchange = "code-sibyl-mq-rabbit-exchange";

    @Autowired
    AmqpAdmin amqpAdmin;

    @Bean()
    Connection connectionMono(RabbitProperties rabbitProperties) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return connectionFactory.newConnection("reactor-rabbit");
    }

    @Bean
    Sender sender(Connection connection) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(Mono.just(connection)));
    }

    @Bean
    Receiver receiver(Connection connection) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(Mono.just(connection)));
    }

    @Bean
    Flux<Delivery> deliveryFlux(Receiver receiver) {
        return receiver.consumeNoAck(QUEUE);
    }

    @PostConstruct
    public void init() {
        TopicExchange exchange = new TopicExchange(Exchange);
        amqpAdmin.declareExchange(exchange);
        Queue queue = new Queue(QUEUE, false, false, false);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("demo.routingKey"));

    }

    @PreDestroy
    public void close() throws Exception {
        r.getBean(Connection.class).close();
    }

}
