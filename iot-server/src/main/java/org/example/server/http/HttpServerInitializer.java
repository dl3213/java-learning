package org.example.server.http;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //http的编码解码器
        socketChannel.pipeline().addLast("httpCodec", new HttpServerCodec());
        socketChannel.pipeline().addLast("httpServerHandler", new HttpServerHandler());
    }
}
