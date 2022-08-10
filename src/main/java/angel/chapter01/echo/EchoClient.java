package angel.chapter01.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    public static void main(String[] args) throws Exception{
        //Client용으로 사용하는 EventLoopGroup는 하나만 생성하여 사용하면 된다.
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //Client용으로는 ServerBootStrap이 아닌 그냥 Bootstrap을 사용한다.
            Bootstrap b = new Bootstrap();
            b.group(group)
                    //Client용으로는 NioSocketChannel을 사용함
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoClientHandler());
                        }
                    });
            //Server에서는 bind메서드를 활용해 사용할 포트를 지정해두고 오픈 하지만
            //client에서는 어디로 연결을 맺을지 connect메서드를 활용해 ip와 port를 입력받는다.
            ChannelFuture f = b.connect("localhost",18888).sync();
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
}
