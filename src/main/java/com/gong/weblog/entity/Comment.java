package com.gong.weblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 父评论
     */
    private Long parentId;

    /**
     * 评论目标
     */
    private Long toId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论类型 1 一级评论 2 二级评论 3 二级评论回复
     */
    private String commentType;

    /**
     * 评论创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}