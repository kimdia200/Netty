package angel.chapter04.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

/**
 * 1. HttpServerCodec으로부터 수신된 channelRead 이벤트를 처리해야 하므로
 * ChannelInboundHandlerAdapter추상 클래스를 상속 받는다.
 */
public class HttpHelloWorldServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 2. 웹브라우저로 전송할 Hello World메시지를 상수로 선언햇다.
     */
    private static final byte[] CONTENT = {'H','e','l','l','o','W','o','r','l','d'};
    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");

    /**
     * 3. 웹브라우저로부터 데이터가 모두 수시노디었을 때 채널 버퍼의 내용을 웹 브라우저로 전송한다.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 4. HttpServerCodec으로부터 수신된 channelRead이벤트를 처리하려면 channelRead오버라이드 한다.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 5. 수신된 객체가 httpRequest일 때 HttpResponse객체를 생성하고 헤더와 Hello World메시지를 저장한다.
         */
        if(msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;
            if(HttpHeaders.is100ContinueExpected(req)){
                ctx.write(
                        new DefaultFullHttpResponse(
                                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE
                        )
                );
            }
            boolean keepAlive = HttpHeaders.isKeepAlive(req);
            // public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content)
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(CONTENT)
                    //Unpooled.wrappedBuffer(CONTENT)는 CONTENT 배열을 ByteBuf로 변환 하는 메서드
            );

            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            //helloworld == 10글자라서 Content_length 찍으면 10 나옴
//            System.out.println("CONTENT_LENGTH = "+response.content().readableBytes());

            if(!keepAlive){
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                System.out.println("NOT KEEP-ALIVE");
            }else{
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
                System.out.println("KEEP-ALIVE");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
