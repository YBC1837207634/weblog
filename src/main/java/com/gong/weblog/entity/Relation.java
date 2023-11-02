package com.gong.weblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName relation
 */
@TableName(value ="relation")
@Data
public class Relation implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 关注者id
     */
    private Long followersId;

    /**
     * 被关注者id
     */
    private Long goalId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}