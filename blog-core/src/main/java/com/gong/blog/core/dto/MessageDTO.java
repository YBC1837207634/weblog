package com.gong.blog.core.dto;

import lombok.Data;

@Data
public class MessageDTO {

    /**
     * 接收者id
     */
    private Long receiverId;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String msgType;

    private String extraInfo;

}
