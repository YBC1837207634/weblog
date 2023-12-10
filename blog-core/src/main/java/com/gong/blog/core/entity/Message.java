package com.gong.blog.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * 
 * @TableName message
 */
@TableName(value ="message")
public class Message implements Serializable {
    /**
     * id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 发送者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;

    /**
     * 接收者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;

    /**
     * 接收者状态
     */
    private String receiverType;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息状态
     */
    private String msgStatus;

    /**
     * 发送时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sendTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    public Long getId() {
        return id;
    }

    /**
     * id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 发送者id
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 发送者id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 接收者id
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * 接收者id
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 接收者状态
     */
    public String getReceiverType() {
        return receiverType;
    }

    /**
     * 接收者状态
     */
    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    /**
     * 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 消息类型
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * 消息类型
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * 消息状态
     */
    public String getMsgStatus() {
        return msgStatus;
    }

    /**
     * 消息状态
     */
    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    /**
     * 发送时间
     */
    public Long getSendTime() {
        return sendTime;
    }

    /**
     * 发送时间
     */
    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Message other = (Message) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getReceiverId() == null ? other.getReceiverId() == null : this.getReceiverId().equals(other.getReceiverId()))
            && (this.getReceiverType() == null ? other.getReceiverType() == null : this.getReceiverType().equals(other.getReceiverType()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getMsgType() == null ? other.getMsgType() == null : this.getMsgType().equals(other.getMsgType()))
            && (this.getMsgStatus() == null ? other.getMsgStatus() == null : this.getMsgStatus().equals(other.getMsgStatus()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getReceiverId() == null) ? 0 : getReceiverId().hashCode());
        result = prime * result + ((getReceiverType() == null) ? 0 : getReceiverType().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getMsgType() == null) ? 0 : getMsgType().hashCode());
        result = prime * result + ((getMsgStatus() == null) ? 0 : getMsgStatus().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", receiverType=").append(receiverType);
        sb.append(", content=").append(content);
        sb.append(", msgType=").append(msgType);
        sb.append(", msgStatus=").append(msgStatus);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}