package com.gong.weblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 评论数量
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentCounts;

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

    private Long likeCount;

    private Long collectCount;

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