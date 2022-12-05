package com.ld.poetry.im.http.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天群
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_group")
public class ImChatGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 群名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 群主用户ID
     */
    @TableField("master_user_id")
    private Integer masterUserId;

    /**
     * 类型[1:聊天群，2:话题]
     */
    @TableField("group_type")
    private Integer groupType;

    /**
     * 群头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 公告
     */
    @TableField("notice")
    private String notice;

    /**
     * 进入方式[0:无需验证，1:需要群主或管理员同意]
     */
    @TableField("in_type")
    private Boolean inType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 是否启用[0:未删除，1:已删除]
     */
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
