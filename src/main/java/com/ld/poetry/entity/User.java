package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 是否启用[0:否，1:是]
     */
    @TableField("user_status")
    private Boolean userStatus;

    /**
     * 性别[1:男，2:女，0:保密]
     */
    @TableField("gender")
    private Integer gender;

    /**
     * openId
     */
    @TableField("open_id")
    private String openId;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 用户类型[0:admin，1:管理员，2:普通用户]
     */
    @TableField("user_type")
    private Integer userType;

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

    /**
     * 最终修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 是否启用[0:未删除，1:已删除]
     */
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
