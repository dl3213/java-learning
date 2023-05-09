package me.sibyl.mq.rabbit.provider;

import me.sibyl.mq.rabbit.test.MqTestConfig;
import me.sibyl.mq.rabbit.topic.TopicMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Stream;

/**
 * @Classname TopicMqProvider
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 22:43
 */
@Service
public class TopicMqProvider {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        rabbitTemplate.convertAndSend(
                TopicMqConfig.topic_exchange,
                TopicMqConfig.topic_routing_key + ".test",
                msg
        );
        rabbitTemplate.convertAndSend(
                TopicMqConfig.topic_exchange,
                TopicMqConfig.topic_routing_key + ".test.topic",
                msg
        );
    }

    public void batchSend() {
        Stream
                .iterate(1, i -> i + 1)
                .limit(12)
                .forEach(item -> {
                    rabbitTemplate.convertAndSend(
                            MqTestConfig.batch_exchange,
                            MqTestConfig.routing_key,
                            "msg" + item
                    );
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
