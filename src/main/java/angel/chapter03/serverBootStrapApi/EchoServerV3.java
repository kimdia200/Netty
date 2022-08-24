package angel.chapter03.serverBootStrapApi;

import angel.chapter01.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServerV3 {
    public static void main(String[] args) throws Exception{
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipe = ch.pipeline();
                            /**
                             * EchoServerV2와 비교했을때 handler메서드 대신 childHandler에 파이프라인을 통해
                             * LoggingHanlder를 실행 시켰다.
                             */
                            pipe.addLast(new LoggingHandler(LogLevel.INFO));
                            pipe.addLast(new EchoServerHandler());
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
