package com.gong.blog.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.core.entity.SessionRecord;
import com.gong.blog.core.vo.SessionRecordVo;

/**
* @author asus
* @description 针对表【session_record】的数据库操作Service
* @createDate 2023-12-04 17:54:40
*/
public interface SessionRecordService extends IService<SessionRecord> {

    IPage<SessionRecordVo> getRecordList(PageParams params);

    void recordSession(Long talkerUid);

    void removeRecord(Long receiverId);
}
