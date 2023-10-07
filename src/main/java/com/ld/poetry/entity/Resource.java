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
 * 资源信息
 * </p>
 *
 * @author sara
 * @since 2022-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resource")
public class Resource implements Serializable {

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
     * 资源类型
     */
    @TableField("type")
    private String type;

    /**
     * 是否启用[0:否，1:是]
     */
    @TableField("status")
    private Boolean status;

    /**
     * 存储平台
     */
    @TableField("store_type")
    private String storeType;

    /**
     * 资源路径
     */
    @TableField("path")
    private String path;

    /**
     * 资源内容的大小，单位：字节
     */
    @TableField("size")
    private Integer size;

    /**
     * 资源的 MIME 类型
     */
    @TableField("mime_type")
    private String mimeType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
