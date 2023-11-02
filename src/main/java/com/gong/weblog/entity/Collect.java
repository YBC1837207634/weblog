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
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 1 收藏夹 2 收藏的项目
     */
    private String collectType;

    /**
     * 收藏的项目id
     */
    private Long itemId;

    /**
     * 项目类型 a 文章
     */
    private String itemType;

    /**
     * 名称
     */
    private String collectName;

    /**
     * 收藏夹创建时间 或 项目收藏时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}