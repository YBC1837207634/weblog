package com.gong.weblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect implements Serializable {
    /**
     * 
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 1 收藏夹 2 收藏的项目
     */
    private String collectType;

    /**
     * 收藏的项目id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemId;

    /**
     * 项目类型 a 文章
     */
    private String itemType;

    /**
     * 所属收藏夹id
     */
    public Long affiliationId;

    /**
     * 收藏夹是否公开
     */
    private Integer common;

    /**
     * 名称
     */
    private String collectName;

    /**
     * 收藏夹创建时间 或 项目收藏时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}