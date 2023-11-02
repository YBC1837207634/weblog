package com.gong.weblog.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ArticleForm {

    @Range(min = 1000000000000000000L, message = "id格式有误")
    private Long articleId;

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
    private String img;

    // 是否可以匿名查看
    private boolean anonymous = false;

    // 是否公开
    @Range(min = 0, max=1, message = "格式错误")
    private Integer common = 1;

}
