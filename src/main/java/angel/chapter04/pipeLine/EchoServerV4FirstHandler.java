package angel.chapter04.pipeLine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EchoServerV4FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("FirstHandler channelRead : "+readMessage.toString(StandardCharsets.UTF_8));
        ctx.write(msg);
        ctx.fireChannelRead(msg);
    }
}
