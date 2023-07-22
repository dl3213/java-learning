package me.sibyl.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class TestServerChannelInitializer extends ChannelInitializer {
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new TestServerHandler());

    }
}

