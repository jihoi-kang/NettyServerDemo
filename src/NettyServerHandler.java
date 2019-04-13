package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("channelRead");
		if (msg instanceof MessageHolder) {
			MessageHolder messageHolder = (MessageHolder) msg;

			switch (messageHolder.getType()) {
			case ProtocolHeader.CONN:
				NettyServer.clients.put(messageHolder.getChatLogs().getBody(), ctx.channel());
				((MessageHolder) msg).setSign(ProtocolHeader.NOTICE);
				ctx.writeAndFlush(msg);
				break;

			default:
				break;
			}
		}

	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("channelReadComplete");
		ctx.flush();
	}
}
