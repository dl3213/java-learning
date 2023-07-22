package me.sibyl.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class TestClientChannelInitializer extends ChannelInitializer {
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new TestClientHandler());
    }
}


