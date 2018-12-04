package com.trey.chat.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataContent implements Serializable {

    private static final long serialVersionUID = 527938305307183257L;
    // 动作类型
    private Integer action;

    // 用户的聊天内容
    private ChatMsgObj chatMsgObj;

    // 扩展字段
    private String extend;

}