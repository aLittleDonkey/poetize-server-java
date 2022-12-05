package com.ld.poetry.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GroupVO {

    private Integer id;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群头像
     */
    private String avatar;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 类型[1:聊天群，2:话题]
     */
    private Integer groupType;

    /**
     * 公告
     */
    private String notice;

    /**
     * 是否群主[0:否，1:是]
     */
    private Boolean masterFlag;

    /**
     * 是否管理员[0:否，1:是]
     */
    private Boolean adminFlag;

    /**
     * 进入方式[0:无需验证，1:需要群主或管理员同意]
     */
    private Boolean inType;

    /**
     * 用户状态[1:审核通过，2:禁言]
     */
    private Integer userStatus;

    /**
     * 加群时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
