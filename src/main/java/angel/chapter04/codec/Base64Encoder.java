package angel.chapter04.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Dialect;

import java.util.List;

/**
 * Base64 인코더 클래스다.
 *
 * 1. MessageToMessageEncoder 추상클래스를 상속받는다. (코덱은 결국 Handler의 구현체이다)
 * 2. 부모클래스의 추상메서드인 encode메서드를 필수로 구현 해야한다.
 *
 *
 * 부가정보.
 * MessageToMessageEncoder
 * 1. MessageToMessageEncoder도 코덱일 뿐이므로 ChannelOutboundHandlerAdapter 클래스를 상속받는다
 * 2. 부모 Adapter클래스의 write 이벤트 메서드를 오버라이드 했다.
 * 3. 자체 메서드인 encode를 추상화 하였다.
 * 4. 오버라이딩한 write메서드에서 encode메서드를 호출하여 사용한다.
 * 5. encode메서드의 경우 자식 클래스에서 정의하게 된다.
 */
public class Base64Encoder extends MessageToMessageEncoder<ByteBuf> {
    private final boolean breakLines;
    private final Base64Dialect dialect;

    //생성자 오버로딩
    public Base64Encoder (){
        this(true);
    }
    public Base64Encoder(boolean breakLines){
        this(breakLines, Base64Dialect.STANDARD);
    }
    public Base64Encoder(boolean breakLines, Base64Dialect dialect){
        if(dialect == null){
            throw new NullPointerException("dialect");
        }

        this.breakLines = breakLines;
        this.dialect = dialect;
    }

    //MessageToMessageEncoder 추상 클래스의 필수 구현 메서드
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(Base64.encode(msg, msg.readerIndex(), msg.readableBytes(),breakLines, dialect));
    }
}
