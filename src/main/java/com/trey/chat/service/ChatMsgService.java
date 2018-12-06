package com.trey.chat.service;

import com.trey.chat.enums.MsgSignFlagEnum;
import com.trey.chat.model.ChatMsg;
import com.trey.chat.netty.ChatMsgObj;
import com.trey.chat.repository.ChatMsgRepository;
import org.joda.time.DateTime;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMsgService {
    @Autowired
    private ChatMsgRepository chatMsgRepository;

    /**
     * 保存消息
     *
     * @param chatMsgObj
     * @return
     */
//    @Transactional(propagation = Propagation.REQUIRED)
    public ChatMsg saveChatMsg(ChatMsgObj chatMsgObj) {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setId(Sid.nextShort());
        chatMsg.setAcceptUserId(chatMsgObj.getReceiverId());
        chatMsg.setSendUserId(chatMsgObj.getSenderId());
        chatMsg.setMsg(chatMsgObj.getMsg());
        chatMsg.setSignFlag(MsgSignFlagEnum.unsign.getType());
        chatMsg.setCreateTime(DateTime.now().toDate());
        return chatMsgRepository.save(chatMsg);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMsgSigned(List<String> msgIdList) {
        msgIdList.stream().forEach(msgId -> chatMsgRepository.updateSignFlag(msgId));
    }
}