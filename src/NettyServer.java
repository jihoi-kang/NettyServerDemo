package server;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer {
	/** key => user_id, value => channel */
	public static ConcurrentHashMap<String, Channel> clients;

	private NettyServer() {
		clients = new ConcurrentHashMap<>();
	}
	
	private void run() throws Exception {
		System.out.println("[Server is Started]");
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
//			ServerBootstrap은 서버를 설정하는 도우미 클래스
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
//				들어오는 연결을 허용하기 위해 새 채널을 인스턴스화하는데 사용되는 NioServerSocketChannel 클래스 사용
				.channel(NioServerSocketChannel.class)
//				지정 핸들러는 항상 새로 승인 된 채널에 의해 평가
//				ChannelInitializer는 사용자가 새 채널을 구성하는데 도움
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
//						pipeline.addLast(new LoggingHandler(LogLevel.INFO));
						pipeline.addLast("IdleStateHandler", new IdleStateHandler(6, 0, 0));
		                pipeline.addLast("ProtocolDecoder", new ProtocolDecoder());
		                pipeline.addLast("ProtocolEncoder", new ProtocolEncoder());
		                pipeline.addLast("NettyServerHandler", new NettyServerHandler());
					}
				})
//				채널 구현 관련된 매개 변수 설정 가능
//				ex)tcpNoDelay, keepAlive와 같은 소켓 옵션 설정 가능
				.option(ChannelOption.SO_BACKLOG, 128)
//				option()은 들어오는 연결을 허용하는 NioServerSocketChannel
//				childOption()은 NioServerSocketChannel의 부모 ServerChannel에서 허용하는 채널
				.childOption(ChannelOption.SO_KEEPALIVE, true);
		
//			포트를 바인딩하고 서버를 시작
//			바인드하여 들어오는 연결 수락
			ChannelFuture f = b.bind(8080).sync();
			
//			서버 소켓이 닫힐 때까지 기다린다.
//			이 예에서는 이런 일이 발생하지 않지만 정상적으로 수행 할 수 있음
//			서버를 종료하십시오.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		NettyServer server = new NettyServer();
		server.run();
	}

}
