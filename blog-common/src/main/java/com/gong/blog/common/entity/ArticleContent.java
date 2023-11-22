package com.gong.blog.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * @TableName article_content
 */
@TableName(value ="article_content")
@Getter
@Setter
public class ArticleContent implements Serializable {
    /**
     * id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 关联 文章id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * html内容
     */
    private String contentHtml;

    private Integer anonymous;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}