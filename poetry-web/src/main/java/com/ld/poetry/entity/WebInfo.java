package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 网站信息表
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("web_info")
public class WebInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网站名称
     */
    @TableField("web_name")
    private String webName;

    /**
     * 网站信息
     */
    @TableField("web_title")
    private String webTitle;

    /**
     * 公告
     */
    @TableField("notices")
    private String notices;

    /**
     * 页脚
     */
    @TableField("footer")
    private String footer;

    /**
     * 背景
     */
    @TableField("background_image")
    private String backgroundImage;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 随机头像
     */
    @TableField("random_avatar")
    private String randomAvatar;

    /**
     * 随机名称
     */
    @TableField("random_name")
    private String randomName;

    /**
     * 随机封面
     */
    @TableField("random_cover")
    private String randomCover;

    /**
     * 看板娘消息
     */
    @TableField("waifu_json")
    private String waifuJson;

    /**
     * 是否启用[0:否，1:是]
     */
    @TableField("status")
    private Boolean status;

    @TableField(exist = false)
    private String historyAllCount;

    @TableField(exist = false)
    private String historyDayCount;

    @TableField(exist = false)
    private String defaultStoreType;
}
