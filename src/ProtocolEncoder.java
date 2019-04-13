package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<MessageHolder> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageHolder msg, ByteBuf out) throws Exception {
    	ChatLogs chatLogs = msg.getChatLogs();
        String body = Serializer.serialize(chatLogs);

        if(body == null)
            throw new NullPointerException("Body is null");

        byte[] bytes = body.getBytes("utf-8");

//        프로토콜 헤더 만들기
        out.writeShort(ProtocolHeader.START)
                .writeByte(msg.getSign())
                .writeByte(msg.getType())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
