package com.ld.poetry.entity;

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
 * 历史信息
 * </p>
 *
 * @author sara
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history_info")
public class HistoryInfo implements Serializable {

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
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * 国家
     */
    @TableField("nation")
    private String nation;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
