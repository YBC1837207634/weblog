package com.gong.blog.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName article
 */
@TableName(value ="article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable {

    public Article(Long id, Integer commentCounts, Integer viewCounts, Integer likeCount, Integer collectCount) {
        this.id = id;
        this.commentCounts = commentCounts;
        this.viewCounts = viewCounts;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
    }

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 评论数量
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer commentCounts;

    /**
     * 简介
     */
    private String summary;

    /**
     * 标题
     */
    private String title;

    /**
     * 浏览数量
     */
    private Integer viewCounts;

    /**
     * 是否置顶
     */
    private Integer weight;

    private String img;

    private Integer likeCount;

    private Integer collectCount;

    private String status;

    /**
     * 作者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorId;

    /**
     * 内容id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bodyId;

    /**
     * 类别id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    private Integer anonymous;

    /**
     * 是否公开
     */
    private Integer common;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}