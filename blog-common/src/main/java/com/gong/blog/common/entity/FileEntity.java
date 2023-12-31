package com.gong.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName files
 */
@TableName(value ="files")
@Data
public class FileEntity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 字节
     */
    private Long size;

    /**
     * md5
     */
    private String md5;

    /**
     * url
     */
    private String url;

    /**
     * 是否删除

     */
    private Integer deleted;

    /**
     * 是否可用
     */
    private Integer status;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新日期
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}