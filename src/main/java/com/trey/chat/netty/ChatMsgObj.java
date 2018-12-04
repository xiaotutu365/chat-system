package com.trey.chat.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMsgObj implements Serializable {
    private static final long serialVersionUID = 5141047039290441346L;

    private String senderId;

    private String receiverId;

    private String msg;

    private String msgId;
}