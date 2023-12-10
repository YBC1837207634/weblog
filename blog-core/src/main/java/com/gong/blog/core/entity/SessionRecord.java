package com.gong.blog.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 
 * @TableName session_record
 */
@TableName(value ="session_record")
public class SessionRecord implements Serializable {
    /**
     * 会话id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 发送人id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;

    /**
     * 接收人id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;

    /**
     * 会话发起时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionTime;

    /**
     * 是否置顶
     */
    private boolean top;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 会话id
     */
    public Long getId() {
        return id;
    }

    /**
     * 会话id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 发送人id
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 发送人id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 接收人id
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * 接收人id
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 会话发起时间
     */
    public Long getSessionTime() {
        return sessionTime;
    }

    /**
     * 会话发起时间
     */
    public void setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }
}