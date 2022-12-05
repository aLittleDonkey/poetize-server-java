package com.ld.poetry.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GroupUserVO {

    private Integer id;

    private Integer groupId;

    private Integer userId;

    private String username;

    private String avatar;

    private Integer verifyUserId;

    private Boolean adminFlag;

    private String remark;

    private Integer userStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String groupName;
}
