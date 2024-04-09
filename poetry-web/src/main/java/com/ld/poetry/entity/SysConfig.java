package com.ld.poetry.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author sara
 * @since 2024-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_config")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("config_name")
    private String configName;

    /**
     * 键名
     */
    @TableField("config_key")
    private String configKey;

    /**
     * 键值
     */
    @TableField("config_value")
    private String configValue;

    /**
     * 1 私用 2 公开
     */
    @TableField("config_type")
    private String configType;


}
