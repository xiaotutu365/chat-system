package com.trey.chat.model;

import java.util.Date;

public class ChatMsg {
    /** */
    private String id;

    /** */
    private String sendUserId;

    /** */
    private String acceptUserId;

    /** */
    private String msg;

    /** */
    private Integer signFlag;

    /** */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sendUserId=").append(sendUserId);
        sb.append(", acceptUserId=").append(acceptUserId);
        sb.append(", msg=").append(msg);
        sb.append(", signFlag=").append(signFlag);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}