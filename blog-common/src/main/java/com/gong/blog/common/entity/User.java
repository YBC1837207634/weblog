package com.gong.blog.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
     */
    @JsonIgnore
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;


    private String gender;

    /**
     * 角色
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 加密盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 背景
     */
    private String background;

    /**
     * 状态
     */
    private String status;

    private String description;

    /**
     * 最后登录时间
     */
    @JsonIgnore
    private Date lastLogin;

    /**
     * 是否删除
     */
    @JsonIgnore
    private Integer deleted;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}