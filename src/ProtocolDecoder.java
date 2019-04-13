package server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;

public class ProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolHeader.HEADER_LENGTH) {
//            패킷 길이가 해더보다 짧음
            return;
        }

//        버퍼의 현재 readerIndex를 마크합니다
//        resetReaderIndex를 호출하여 마크한 readerIndex로 위치 변경이 가능합니다
        in.markReaderIndex();

        if (in.readShort() != ProtocolHeader.START) {
            in.resetReaderIndex();
            return;
        }

        byte sign = in.readByte();
        byte type = in.readByte();

        int bodyLength = in.readInt();

        if (in.readableBytes() != bodyLength) {
//            해더에서 보낸 body 길이 값과 실제 데이터(body)의 길이가 맞지 않음
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[bodyLength];
        in.readBytes(bytes);

        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(sign);
        messageHolder.setType(type);
        messageHolder.setChatLogs(Serializer.deserialize(new String(bytes, "utf-8"), ChatLogs.class));

        out.add(messageHolder);
    }
}
