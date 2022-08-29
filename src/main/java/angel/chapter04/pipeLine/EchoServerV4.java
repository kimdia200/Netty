package angel.chapter04.pipeLine;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServerV4 {
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
                            ChannelPipeline p = ch.pipeline();
                            //EchoServerV33과 마찬가지로 핸들러를 두개로 분리하고 메서드를 각각 구현 했을뿐
                            //동일하게 작동한다.
                            p.addLast(new EchoServerV4FirstHandler());
                            p.addLast(new EchoServerV4SecondHandler());
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
