package angel.chapter03.serverBootStrapApi;

import angel.chapter01.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;

public class EchoServerWithOption {
    public static void main(String[] args) throws Exception{
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(ServerSocketChannel.class)
                    /**
                     * 아래와 같이 옵션을 줄 수 있다.
                     * 옵션의 종류
                     * 1. TCP_NODELAY : 데이터 송수신에 Nagle알고리즘의 비활성화 여부를 지정한다. (기본값 : false 비활성화)
                     *  +) Nagle알고리즘 : 작은 패킷을 합쳐서 나눠 보내는 방식으로 수신여부 체크를 하기 때문에 비교적 오래걸림
                     * 2. SO_KEEPALIVE : 운영체제에서 지정된 시간에 한번씩 keepalive패킷을 상대방에게 전송한다. (기본값 : false 비활성화)
                     * 3. SO_SNDBUF : 상대방으로 송신할 커널 소인 버퍼의 크기
                     * 4. SO_RCVBUF : 상대방으로부터 수신할 커널 수신 버퍼의 크기
                     * 5. SO_REUSEADDR : TIME_WAIT상태의 포트를 서버 소켓에 바인드할 수 있게 한다. (기본값 : false 비활성화)
                     * 6. SO_LINGER : 소켓을 닫을때 커널의 송신 버퍼에 전송되지 않은 데이터의 전송 대기시간을 지정 (기본값 : false 비활성화)
                     * 7. SO_BACKLOG : 동시에 수용 가능한 소켓 연결 요청수
                     */
                    //option 메서드 = 서버 소켓 채널의 소켓 옵션 설정
                    .option(ChannelOption.SO_BACKLOG,1)
                    .childHandler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandler());
                        }
                    });
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
