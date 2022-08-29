package angel.chapter04.pipeLine;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServerV22 {
    public static void main(String[] args) throws Exception{
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 자동 로깅을 해주는 핸들러를 등록 할 수 있다.
                     * 아래 설정한 LoggingHandler는 netty에서 기본 제공하는 [코덱] 이다.
                     * 채널에서 발생하는 양방향 이벤트를 모두 로그로 출력 하도록 구현했다.
                     * 하지만 ServerBootstrap의 handler메서드로 등록된 이벤트 핸들러는 서버 소켓채널에서 발생한 이벤트만을 처리하기 때문에
                     * 모든 로그가 출력되지는 않는다.
                     */
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipe = ch.pipeline();
                            pipe.addLast(new EchoServerHandler22());
                        }
                    });
            ChannelFuture f = b.bind(18888).sync();
            f.channel().closeFuture().sync();
        }finally{
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
