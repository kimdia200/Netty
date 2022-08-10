package angel.chapter01.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * channelActive 메서드는 해당 소켓채널이 최초 활성화 되었을때 실행된다.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String sendMessge = "Hello, Netty";

        //
//        ByteBuf messageBuf = Unpooled.buffer();
        ByteBuf messageBuf = Unpooled.buffer();
        messageBuf.writeBytes(sendMessge.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessge);
        builder.append("]");

        System.out.println(builder.toString());
        ctx.writeAndFlush(messageBuf);
    }

    /**
     * channelRead 부분.
     * Server로 부터 응답값을 받을때 읽는 부분
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    /**
     * 수신된 데이터를 모두 읽었을 때 호출되는 이벤트 메서드.
     * channelRead 메서드의 수행이 종료되고나서 자동으로 호출된다.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //수신된 데이터를 모두 읽은후 서버와 연결된 채널을 닫는다.
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}