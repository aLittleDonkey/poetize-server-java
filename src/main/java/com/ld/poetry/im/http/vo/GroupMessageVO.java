package com.ld.poetry.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GroupMessageVO {

    private Long id;

    /**
     * 群ID
     */
    private Integer groupId;

    /**
     * 发送ID
     */
    private Integer fromId;

    /**
     * 接收ID
     */
    private Integer toId;

    /**
     * 内容
     */
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String avatar;

    private String username;
}
