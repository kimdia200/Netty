package angel.chapter04.pipeLine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * ChannelInboundHandlerAdapter는 channelRead메서드가 구현 되어 있지 않아서 해줘야한다
 */
public class EchoServerHandler22 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
        System.out.println("수신한 문자열 ["+readMessage+"]");
        ctx.writeAndFlush(msg);
    }
}
