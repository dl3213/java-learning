package me.sibyl.mq.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @Classname RabbitProvider
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/19 21:31
 */

public class RabbitProvider {

    private static String queueName = "sibyl_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtil.channel();

        //1队列名称2队列是否持久化3是否多消费者4是否自动删除5其他参数
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();//开启发布确认

        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.err.println("ack => " + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.err.println("nack => " + deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            //1交换机2队列名称3其他参数(可设置持久化消息)
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, next.getBytes());
            System.err.println("send : " + next);
        }
    }
}
