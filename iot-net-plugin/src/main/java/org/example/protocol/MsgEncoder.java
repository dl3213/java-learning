package org.example.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<MsgProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MsgProtocol o, ByteBuf byteBuf) throws Exception {
        System.err.println("MsgEncoder.encode");
        byteBuf.writeInt(o.getLen());
        byteBuf.writeBytes(o.getContent());
    }
}
