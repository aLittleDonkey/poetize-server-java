package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 树洞
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tree_hole")
public class TreeHole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 留言
     */
    @TableField("message")
    private String message;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

}
