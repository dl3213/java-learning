package org.example.server.mqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MqttBroker {
    @Value("${mqtt.serverIp}")
    private String serverIp;
    @Value("${mqtt.serverPort}")
    private int serverPort;


    @Resource
    private MqttBrokerChannelInitializer mqttBrokerChannelInitializer;

    // 多线程事件循环器:接收的连接
    private EventLoopGroup bossGroup;
    // 实际工作的线程组 多线程事件循环器:处理已经被接收的连接
    private EventLoopGroup workGroup;


    private ChannelFuture channelFuture;
    private volatile Channel channel;

    private final AtomicInteger nextMessageId = new AtomicInteger(1);


    public MqttBroker() {
    }

    public void start() {
        log.info("mqtt server start:" + serverPort);

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(mqttBrokerChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(serverPort).sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
