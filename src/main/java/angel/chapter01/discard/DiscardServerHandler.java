package angel.chapter01.discard;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * SimpleChannelInboundHandler의 경우 channelRead 메서드가 이미 구현이 완료 되어있다.
 * 그래서 따로 구현하지 않아도 상관없다.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //아무것도 하지 않는다~
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
