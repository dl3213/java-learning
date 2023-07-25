package org.example.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.protocol.MsgProtocol;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class TcpServerHandler extends SimpleChannelInboundHandler<MsgProtocol> {

    private int count = 0;

    /**
     * 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtocol object) throws Exception {
        String msg = new String(object.getContent(), StandardCharsets.UTF_8);
        System.err.println(msg);
        System.err.println(count);
        count++;

        String retMsg = UUID.randomUUID().toString();
        //ByteBuf response = Unpooled.copiedBuffer(retMsg, StandardCharsets.UTF_8);
        MsgProtocol msgProtocol = new MsgProtocol();
        msgProtocol.setLen(retMsg.getBytes(StandardCharsets.UTF_8).length);
        msgProtocol.setContent(retMsg.getBytes(StandardCharsets.UTF_8));
        channelHandlerContext.writeAndFlush(msgProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
