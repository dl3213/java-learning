package org.example.server.tcp;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final TcpServerHandler tcpServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new MsgDecoder());
        socketChannel.pipeline().addLast(tcpServerHandler);
    }


}
