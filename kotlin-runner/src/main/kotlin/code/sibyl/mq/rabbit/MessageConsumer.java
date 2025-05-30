package code.sibyl.mq.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("t_base_file"),
            exchange = @Exchange(value = RabbitMQConfig.Exchange, type = "topic")
    ), ackMode = "AUTO"
    )
    public void file_handler(String message) {
        log.info("rabbit mq t_base_file_handler -> {}", message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("t_biz_book"),
            exchange = @Exchange(value = RabbitMQConfig.Exchange, type = "topic")
    ), ackMode = "AUTO")
    public void t_biz_book_handler(String message) {
        log.info("rabbit mq t_biz_book_handler -> {}", message);

    }
}
