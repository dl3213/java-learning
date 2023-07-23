package org.example.server.mqtt;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mqtt.message.strategy.MessageStrategy;
import org.example.mqtt.MessageStrategyManager;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


/**
 * 入站处理
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class MqttBrokerChannelHandler extends ChannelInboundHandlerAdapter {

    private final MessageStrategyManager messageStrategyManager;

    /**
     * 读取实际数据
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        MqttMessage mqttMessage = (MqttMessage) msg;
        log.info("--------------------------begin---------------------------*");
        log.info("来自终端:" + channelHandlerContext.channel().remoteAddress());
        log.info("接收消息：" + mqttMessage.toString());
        try {
            MqttMessageType type = mqttMessage.fixedHeader().messageType();
            MessageStrategy messageStrategy = messageStrategyManager.getMessageStrategy(type);
            if (messageStrategy != null) {
                messageStrategy.sendResponseMessage(channelHandlerContext.channel(), mqttMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("--------------------------end---------------------------*");

    }

    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("successful", StandardCharsets.UTF_8));
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
