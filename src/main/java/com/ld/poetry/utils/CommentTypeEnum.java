package com.ld.poetry.utils;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论类型
 */
public enum CommentTypeEnum {

    COMMENT_TYPE_ARTICLE("article", "文章评论"),
    COMMENT_TYPE_MESSAGE("message", "树洞留言"),
    COMMENT_TYPE_LOVE("love", "表白墙留言");

    private final String code;

    private final String desc;

    CommentTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CommentTypeEnum getEnumByCode(String code) {
        if (StringUtils.hasText(code)) {
            CommentTypeEnum[] values = CommentTypeEnum.values();
            for (CommentTypeEnum typeEnum : values) {
                if (typeEnum.getCode().equalsIgnoreCase(code)) {
                    return typeEnum;
                }
            }
        }
        return null;
    }

    public static CommentTypeEnum getEnumByDesc(String desc) {
        if (StringUtils.hasText(desc)) {
            CommentTypeEnum[] values = CommentTypeEnum.values();
            for (CommentTypeEnum typeEnum : values) {
                if (typeEnum.getDesc().equalsIgnoreCase(desc)) {
                    return typeEnum;
                }
            }
        }
        return null;
    }

    public static Map<String, String> getEnumMap() {
        return Arrays.stream(CommentTypeEnum.values()).collect(Collectors.toMap(CommentTypeEnum::getCode, CommentTypeEnum::getDesc));
    }
}