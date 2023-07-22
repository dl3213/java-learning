package me.sibyl.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    private int port;

    private EventLoopGroup bossGroup;// 多线程事件循环器:接收的连接
    private EventLoopGroup workGroup; // 多线程事件循环器:处理已经被接收的连接
    private ChannelFuture channelFuture;

    public TestServer(int port) {
        this.port = port;
    }

    public void start() {

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(port).sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            destroy();
        }
    }

    public void destroy() {
        // 关闭socket
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
