package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 家庭信息
 * </p>
 *
 * @author sara
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("family")
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 背景封面
     */
    @TableField("bg_cover")
    private String bgCover;

    /**
     * 男生头像
     */
    @TableField("man_cover")
    private String manCover;

    /**
     * 女生头像
     */
    @TableField("woman_cover")
    private String womanCover;

    /**
     * 男生昵称
     */
    @TableField("man_name")
    private String manName;

    /**
     * 女生昵称
     */
    @TableField("woman_name")
    private String womanName;

    /**
     * 计时
     */
    @TableField("timing")
    private String timing;

    /**
     * 倒计时标题
     */
    @TableField("countdown_title")
    private String countdownTitle;

    /**
     * 是否启用[0:否，1:是]
     */
    @TableField("status")
    private Boolean status;

    /**
     * 倒计时时间
     */
    @TableField("countdown_time")
    private String countdownTime;

    /**
     * 额外信息
     */
    @TableField("family_info")
    private String familyInfo;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最终修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
