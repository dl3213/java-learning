package org.example.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.example.protocol.MsgDecoder;
import org.example.protocol.MsgEncoder;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline().addLast(new MsgEncoder());
        socketChannel.pipeline().addLast(new MsgDecoder());
        socketChannel.pipeline().addLast(new MyClientHandler());
    }
}
