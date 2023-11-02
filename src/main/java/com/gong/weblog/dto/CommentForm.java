package com.gong.weblog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CommentForm {

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 父评论
     */
    private Long parentId;

    /**
     * 回复目标
     */
    private Long toId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不可为空")
    @Size(max = 1000, message = "不可超过1000个字符")
    private String content;

    /**
     * 评论类型 1 一级评论 2 二级评论 3 二级评论回复
     */
    @Pattern(regexp = "[123]", message = "error")
    private String commentType;

}
