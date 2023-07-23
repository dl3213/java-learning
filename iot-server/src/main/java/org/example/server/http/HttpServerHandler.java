package org.example.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;


@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) httpObject;
            URI uri = new URI(httpRequest.uri());
            log.info("from client[{}]:{}", channelHandlerContext.channel().remoteAddress(), uri.getPath());
            // todo 名单处理,返回处理
            if ("/favicon.ico".equals(uri.getPath())) {
                return;
            }

            // 回复客户端
            ByteBuf byteBuf = Unpooled.copiedBuffer("successful成功", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
