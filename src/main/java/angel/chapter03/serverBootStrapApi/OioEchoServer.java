package angel.chapter03.serverBootStrapApi;

import angel.chapter01.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

// EchoServer 클래스에서 단 3줄만 변경하면 블로킹 서버로 변경 할 수 있음!!!
public class OioEchoServer {
    public static void main(String[] args) throws Exception{
        //1. EventLoopGroup를 Oio(블로킹) 으로 변경
        EventLoopGroup boss = new OioEventLoopGroup(1);
        EventLoopGroup worker = new OioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    //2. 소켓 채널도 블로킹으로 변경
                    .channel(OioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipe = ch.pipeline();
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
