package com.ld.poetry.im.websocket;

public enum ImEnum {
    /**
     * 消息类型
     */
    MESSAGE_TYPE_MSG_SINGLE(1, "单聊"),
    MESSAGE_TYPE_MSG_GROUP(2, "群聊");


    private int code;
    private String msg;

    ImEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
