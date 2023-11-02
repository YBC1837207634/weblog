package com.gong.weblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    /**
     * 标签id
     */
    @TableId
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 可见
     */
    private Integer visible;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}