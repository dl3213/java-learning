package me.sibyl.mq.consumer;

import com.rabbitmq.client.*;
import me.sibyl.mq.util.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Classname Consumer
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/19 21:44
 */

public class RabbitConsumer {

    private static String queueName = "sibyl_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtil.channel();

        //1消费队列2是否自动应答3消费失败回调4取消消费回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.err.println(Thread.currentThread().getName() + " => " + new String(message.getBody()));
            //1消息标记2是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.err.println("CancelCallback");
        };
        System.err.println(Thread.currentThread().getName() + " wait ......");
        boolean autoAck = false;
        channel.basicQos(1);//不公平分发
        channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallback);

    }
}
