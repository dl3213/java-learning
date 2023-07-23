package org.example.server.socket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.server.http.HttpServerHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //http的编码解码器
        socketChannel.pipeline().addLast(new HttpServerCodec());// socket基于http
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());// 块方式写，添加对应处理器
        socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));// http分段传输，将多个段聚合
        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/hello"));// websocket以帧frame传递，将http升级为ws，保存长连接
        socketChannel.pipeline().addLast(new SocketServerHandler());// 业务处理

    }
}
