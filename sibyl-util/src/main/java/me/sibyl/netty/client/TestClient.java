package me.sibyl.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient {
    private String serverIp = "";//服务器的IP
    private int serverPort = 8080;//服务器的端口

    public TestClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void start() throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new TestClientChannelInitializer());
            //连接服务器
            ChannelFuture channelFuture = clientBootstrap.connect(serverIp, serverPort).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

