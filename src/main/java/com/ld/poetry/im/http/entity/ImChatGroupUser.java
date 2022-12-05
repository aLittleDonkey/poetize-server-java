package com.ld.poetry.im.http.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天群成员
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_group_user")
public class ImChatGroupUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 群ID
     */
    @TableField("group_id")
    private Integer groupId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 审核用户ID
     */
    @TableField("verify_user_id")
    private Integer verifyUserId;

    /**
     * 是否管理员[0:否，1:是]
     */
    @TableField("admin_flag")
    private Boolean adminFlag;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 用户状态[0:未审核，1:审核通过，2:禁言]
     */
    @TableField("user_status")
    private Integer userStatus;

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
