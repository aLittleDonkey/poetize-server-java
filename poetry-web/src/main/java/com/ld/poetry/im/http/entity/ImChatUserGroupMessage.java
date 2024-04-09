package com.ld.poetry.im.http.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群聊记录
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_user_group_message")
public class ImChatUserGroupMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群ID
     */
    @TableField("group_id")
    private Integer groupId;

    /**
     * 发送ID
     */
    @TableField("from_id")
    private Integer fromId;

    /**
     * 接收ID
     */
    @TableField("to_id")
    private Integer toId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
