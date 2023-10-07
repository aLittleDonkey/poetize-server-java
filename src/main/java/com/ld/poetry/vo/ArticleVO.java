package com.ld.poetry.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ld.poetry.entity.Label;
import com.ld.poetry.entity.Sort;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ArticleVO {

    private Integer id;

    private Integer userId;

    //查询为空时，随机选择
    private String articleCover;

    @NotBlank(message = "文章标题不能为空")
    private String articleTitle;

    @NotBlank(message = "文章内容不能为空")
    private String articleContent;

    private Integer viewCount;

    private Integer likeCount;

    private Boolean commentStatus;

    private Boolean recommendStatus;

    private String password;

    private String tips;

    private Boolean viewStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String updateBy;

    @NotNull(message = "文章分类不能为空")
    private Integer sortId;

    @NotNull(message = "文章标签不能为空")
    private Integer labelId;

    // 需要查询封装
    private Integer commentCount;
    private String username;
    private Sort sort;
    private Label label;
}
