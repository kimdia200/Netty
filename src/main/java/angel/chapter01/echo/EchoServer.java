package angel.chapter01.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    public static void main(String[] args) throws Exception{
        //Server는 EventLoopGroup를 두개 사용한다.
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        //workerThread의 갯수는 생성자에서도 결정 지어 줄 수 있다.
        //EventLoopGroup worker = new NioEventLoopGroup(worker쓰레드갯수);
        try{
            //Server에서는 ServerBootstrap 클래스 사용함, (client 에서는 Bootstrap)
            ServerBootstrap b = new ServerBootstrap();
            //Server에서는 두개의 NioLoopGroup를 모두 사용
            b.group(boss, worker)
                    //Server에서는 NioServerSocketChannel 클래스 사용
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipe = ch.pipeline();
                            pipe.addLast(new EchoServerHandler());
                        }
                    });

            //Server에서는 bind메서드를 활용하여 서버소켓 포트만 활성화 해둠
            //sync 메서드는 앞의 ChannelFuture 객체의 메서드가 끝나는 순간까지 기다린다.
            ChannelFuture f = b.bind(18888).sync();
            f.channel().closeFuture().sync();
        }finally{
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
