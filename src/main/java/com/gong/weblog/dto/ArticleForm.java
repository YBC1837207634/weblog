package com.gong.weblog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ArticleDTO {

    // 概要
    @Size(max = 255, message = "不可大于255")
    private String summary;

    // 标题
    @NotBlank(message = "标题不可为空")
    @Size(max = 255, message = "标题不可大于255个字符")
    private String title;

    // 标签
    @Size(max = 8, message = "可选标签不超过5个")
    private List<Long> tagIds;

    private Long category = 1L;

    // 内容
    @NotBlank(message = "内容不可为空")
    private String content;

    // 封面
    @Pattern(regexp = "http.*", message = "封面url不合规范")
    private String img;

}
