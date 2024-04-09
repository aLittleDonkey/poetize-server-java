package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文章评论表
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论来源标识
     */
    @TableField("source")
    private Integer source;

    /**
     * 父评论ID
     */
    @TableField("parent_comment_id")
    private Integer parentCommentId;

    /**
     * 评论来源类型
     */
    @TableField("type")
    private String type;

    /**
     * 发表用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 父发表用户名ID
     */
    @TableField("parent_user_id")
    private Integer parentUserId;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论内容
     */
    @TableField("comment_content")
    private String commentContent;

    /**
     * 评论额外信息
     */
    @TableField("comment_info")
    private String commentInfo;

    /**
     * 楼层评论ID
     */
    @TableField("floor_comment_id")
    private Integer floorCommentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
