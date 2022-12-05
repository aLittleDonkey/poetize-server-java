package com.ld.poetry.utils;

public enum CodeMsg {
    SUCCESS(200, "成功！"),
    PARAMETER_ERROR(400, "参数异常！"),
    NOT_LOGIN(300, "未登陆，请登陆后再进行操作！"),
    LOGIN_EXPIRED(300, "登录已过期，请重新登陆！"),
    SYSTEM_REPAIR(301, "系统维护中，敬请期待！"),
    FAIL(500, "服务异常！");


    private int code;
    private String msg;

    CodeMsg(int code, String msg) {
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
