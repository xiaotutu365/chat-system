package com.trey.chat.enums;

public enum MsgSignFlagEnum {
    unsign(0, "未签收"),
    signed(1, "已签收");

    private Integer type;

    private String content;

    MsgSignFlagEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}