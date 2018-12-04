package com.trey.chat.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 聊天消息
 */
@Data
@Entity
@Table(name = "chat_msg")
public class ChatMsg {
    @Id
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    private Integer signFlag;

    private Date createTime;
}