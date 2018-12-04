package websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trey.chat.enums.MsgActionEnum;
import com.trey.chat.model.ChatMsg;
import com.trey.chat.netty.ChatMsgObj;
import com.trey.chat.netty.DataContent;
import com.trey.chat.netty.UserChannelRelation;
import com.trey.chat.service.ChatMsgService;
import com.trey.chat.service.UserService;
import com.trey.chat.utils.SpringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author trey
 * @version V1.0.0
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 用于记录和管理所有客户端的channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接受到的数据：" + content);

        Channel currentChannel = ctx.channel();

        // 1、获取客户端发来的消息
        DataContent dataContent = JSON.parseObject(content, DataContent.class);
        Integer action = dataContent.getAction();
        // 2、判断消息类型，根据不同的类型来处理不同的业务
        if (action == MsgActionEnum.CONNECT.getCode()) {
            // 2.1 当WebSocket第一次open的时候，初始化channel，把用户的channel和userId关联起来
            String senderId = dataContent.getChatMsgObj().getSenderId();
            UserChannelRelation.put(senderId, currentChannel);
            users.stream().forEach(channel -> System.out.println(channel.id().asLongText()));
            UserChannelRelation.output();
        } else if (action == MsgActionEnum.CHAT.getCode()) {
            // 2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
            ChatMsgObj chatMsgObj = dataContent.getChatMsgObj();
            // 保存消息到数据库，并标记为未签收
            ChatMsgService chatMsgService = SpringUtils.getBean(ChatMsgService.class);
            ChatMsg chatMsg = chatMsgService.saveChatMsg(chatMsgObj);
            String msgId = chatMsg.getId();
            chatMsgObj.setMsgId(msgId);
            // 发送消息，从全局用户Channel关系中获取接收方的channel
            Channel receiverChannel = UserChannelRelation.get(chatMsgObj.getReceiverId());
            if (receiverChannel == null) {
                // TODO 用户离线，推送消息
            } else {
                Channel findChannel = users.find(receiverChannel.id());
                if (findChannel != null) {
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatMsgObj)));
                } else {
                    // TODO 用户离线，推送消息
                }
            }
        } else if (action == MsgActionEnum.SIGNED.getCode()) {
            // 2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            ChatMsgService chatMsgService = SpringUtils.getBean(ChatMsgService.class);
            // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdStr = dataContent.getExtend();
            String[] msgIdArr = msgIdStr.split(",");
            List<String> msgIdList = new ArrayList<>();
            // 过滤为空的，将非空的加入List中
            Arrays.stream(msgIdArr).filter(Objects::nonNull).forEach(msgIdList::add);
            if (msgIdList.size() > 0) {
                // 批量签收
                chatMsgService.updateMsgSigned(msgIdList);
            }
        } else if (action == MsgActionEnum.KEEPALIVE.getCode()) {
            // 2.4 心跳类型的消息
        }

        for (Channel channel : users) {
            channel.writeAndFlush(
                    new TextWebSocketFrame("服务器[" + ctx.channel().remoteAddress() + "]在[" + DateTime.now() + "]接收到消息，消息为:[" + content + "]"));
        }

        // 该行和上面的效果等价的
        // users.writeAndFlush(new TextWebSocketFrame("[服务器zai ：]" + LocalDateTime.now() + "接收到消息， 消息为：" + content));

    }

    /**
     * 当客户端连接到服务端之后(打开连接)，获取客户端的channel，并且放到ChannelGroup中去进行管理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id为：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id为：" + ctx.channel().id().asShortText());
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel）
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}