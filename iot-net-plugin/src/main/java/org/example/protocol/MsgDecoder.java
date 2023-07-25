package org.example.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MsgDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.err.println("MsgDecoder.decode");


        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);

        MsgProtocol msgProtocol = new MsgProtocol();
        msgProtocol.setLen(len);
        msgProtocol.setContent(bytes);
        list.add(msgProtocol);
    }
}
