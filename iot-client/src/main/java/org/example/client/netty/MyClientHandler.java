package org.example.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.protocol.MsgProtocol;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;


@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class MyClientHandler extends SimpleChannelInboundHandler<MsgProtocol> {


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
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            String msg = "client => " + i;
            //ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
            MsgProtocol protocol = new MsgProtocol();
            protocol.setLen(msg.getBytes(StandardCharsets.UTF_8).length);
            protocol.setContent(msg.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(protocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
