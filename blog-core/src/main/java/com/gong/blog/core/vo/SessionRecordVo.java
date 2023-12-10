package com.gong.blog.core.vo;

import com.gong.blog.core.entity.SessionRecord;
import lombok.Data;

@Data
public class SessionRecordVo extends SessionRecord {

    private static final long serialVersionUID = 1L;

//    private UserVo user;

    private MessageVo lastMsg;
}
