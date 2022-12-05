package com.ld.poetry.utils;

public enum PoetryEnum {
    /**
     * 状态
     */
    STATUS_ENABLE(1, "启用"),
    STATUS_DISABLE(0, "禁用"),

    /**
     * 公开
     */
    PUBLIC(1, "所有人可见"),
    PRIVATE(0, "仅自己可见"),

    /**
     * 用户性别
     */
    USER_GENDER_BOY(1, "男"),
    USER_GENDER_GIRL(2, "女"),
    USER_GENDER_NONE(0, "保密"),


    /**
     * 分类类型
     */
    SORT_TYPE_BAR(0, "导航栏分类"),
    SORT_TYPE_NORMAL(1, "普通分类"),


    /**
     * 用户类型
     */
    USER_TYPE_ADMIN(0, "站长"),
    USER_TYPE_DEV(1, "管理员"),
    USER_TYPE_USER(2, "用户");


    private int code;
    private String msg;

    PoetryEnum(int code, String msg) {
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
