package org.example.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "http", name = "enable", havingValue = "true", matchIfMissing = false)
public class HttpServer {

    @Value("${http.server-ip}")
    private String serverIp;
    @Value("${http.server-port}")
    private int serverPort;
    private EventLoopGroup bossGroup;
    // 实际工作的线程组 多线程事件循环器:处理已经被接收的连接
    private EventLoopGroup workGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public void start() {

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup) //设置线程组
                    .channel(NioServerSocketChannel.class)// 服务器的通道实现
                    //.option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列等待的格式
                    //.childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new HttpServerInitializer())// 给workGroup的EventLoop对应的管道设置处理器
            ;

            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(serverPort).sync();

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("http server started in " + serverPort);
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
