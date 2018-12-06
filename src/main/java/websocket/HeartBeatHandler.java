package websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳
 * @author trey
 * @version V1.0.0
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE) {
                // 读空闲
                System.out.println("读空闲...");
            } else if(event.state() == IdleState.WRITER_IDLE) {
                // 写空闲
                System.out.println("写空闲...");
            } else if(event.state() == IdleState.ALL_IDLE) {
                System.out.println("读写空闲...");
                System.out.println("关闭之前，handler通道数：" + ChatHandler.getUsers().size());
                // 如果在一段时间内，读写都空闲，则需要关闭对应的通道
                Channel channel = ctx.channel();
                System.out.println(channel.id());
                // channel.close();
                System.out.println("关闭之后，handler通道数：" + ChatHandler.getUsers().size());
            }
        }
    }
}