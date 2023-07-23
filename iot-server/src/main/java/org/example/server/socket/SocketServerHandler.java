package org.example.server.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class SocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame object) throws Exception {
        log.info("from client[{}]:{}", channelHandlerContext.channel().remoteAddress(), object.text());
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("server: successful at " + LocalDateTime.now()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("[handlerAdded]{}", ctx.channel().id().asLongText());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("[handlerRemoved]{}", ctx.channel().id().asLongText());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { //
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
