package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("[Server] " + ctx.channel().remoteAddress() + " 클라이언트가 접속하였습니다.");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof MessageHolder) {
			MessageHolder messageHolder = (MessageHolder) msg;
			System.out.println("[받은 메시지]\n" + Serializer.serialize(messageHolder));

			switch (messageHolder.getType()) {
			case ProtocolHeader.CONN:
				NettyServer.clients.put(messageHolder.getChatLogs().getBody(), ctx.channel());
				messageHolder.setSign(ProtocolHeader.CALLBACK);
				ctx.writeAndFlush(messageHolder);
				break;

			default:
				break;
			}
			
			System.out.println("== 접속중인 클라이언트 현황 == ");
            for (String key : NettyServer.clients.keySet()) {
                System.out.print("key=" + key);
                System.out.println(" value=" + NettyServer.clients.get(key).toString());
            }
		}

	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println("[Server] " + ctx.channel().remoteAddress() + " 클라이언트 접속이 끊어졌습니다.");
	}
}
