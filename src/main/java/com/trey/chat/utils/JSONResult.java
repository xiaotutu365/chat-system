package com.trey.chat.utils;

/**
 * 自定义响应数据结构
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 */
public class JSONResult {
    private Integer status;

    private String msg;

    private Object data;

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static JSONResult build(Integer status, String msg, Object data) {
        return new JSONResult(status, msg, data);
    }

    public static JSONResult OK(Object data) {
        return new JSONResult(200, "操作成功", data);
    }
}