package angel.chapter04.codec.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpHelloWorldServerInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if(sslCtx != null){
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        /**
         * 두개의 핸들러 순서를 변경하면 기존과 같이 동작하지 않는다.
         * 이유는 HttpHelloWorldServerHandler에서는 read이후 msg객체를 fire메서드로 전달 해주지 않지만
         * HttpServerCodec에서는 부모클래스에서 read이후 다음 핸들러로 이벤트를 전달 해주지 않기 때문
         */

        //네티가 제공하는 http 기본 코덱이다
        p.addLast(new HttpServerCodec());
        //httpServerCodec이 수신한 이벤트와 데이터를 처리하여 http객체로 변환한 다음 channelRead이벤트를 받아서 처리하는 핸들러
        p.addLast(new HttpHelloWorldServerHandler());
    }
}
