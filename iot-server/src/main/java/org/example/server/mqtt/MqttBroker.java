package org.example.server.mqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简易mqtt-server
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "mqtt", name = "enable", havingValue = "true", matchIfMissing = false)
public class MqttBroker {
    @Value("${mqtt.server-ip}")
    private String serverIp;
    @Value("${mqtt.server-port}")
    private int serverPort;
    private final MqttBrokerChannelInitializer mqttBrokerChannelInitializer;

    // 多线程事件循环器:接收的连接
    private EventLoopGroup bossGroup;
    // 实际工作的线程组 多线程事件循环器:处理已经被接收的连接
    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;
    private volatile Channel channel;

    private final AtomicInteger nextMessageId = new AtomicInteger(1);

    public void start() {

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup) //设置线程组
                    .channel(NioServerSocketChannel.class)// 服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列等待的格式
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(mqttBrokerChannelInitializer)// 给workGroup的EventLoop对应的管道设置处理器
            ;

            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(serverPort).sync();

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("mqtt server started in " + serverPort);
    }

    public void stop() {
        // 关闭channel
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        bossGroup = null;
        workGroup = null;

    }


}
