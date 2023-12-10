package com.gong.blog.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.core.entity.SessionRecord;
import com.gong.blog.core.form.WhisperForm;
import com.gong.blog.core.mapper.SessionRecordMapper;
import com.gong.blog.core.service.MessageService;
import com.gong.blog.core.service.SessionRecordService;
import com.gong.blog.core.vo.MessageVo;
import com.gong.blog.core.vo.SessionRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author asus
* @description 针对表【session_record】的数据库操作Service实现
* @createDate 2023-12-04 17:54:40
*/
@Service
public class SessionRecordServiceImpl extends ServiceImpl<SessionRecordMapper, SessionRecord> implements SessionRecordService{

    @Autowired
    private SessionRecordMapper sessionRecordMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 获取会话记录
     */
    @Override
    public IPage<SessionRecordVo> getRecordList(PageParams params) {
        IPage<SessionRecord> page = new Page<>(params.getPageNum(), params.getPageSize());
        sessionRecordMapper.selectPage(page, Wrappers
                .<SessionRecord>lambdaQuery()
                .eq(SessionRecord::getSenderId, UserContextUtils.getId())
                .orderByDesc(SessionRecord::getSessionTime));
        List<SessionRecordVo> sessionRecordVoList = copyVOBatch(page.getRecords());
        IPage<SessionRecordVo> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage, "records");
        voPage.setRecords(sessionRecordVoList);
        return voPage;
    }

    /**
     * 记录会话
     */
    @Override
    public void recordSession(Long talkerUid) {
        if (talkerUid.equals(UserContextUtils.getId()))
            return;
        User user = userService.getById(talkerUid);
        if (user != null) {
            SessionRecord sessionRecord = new SessionRecord();
            sessionRecord.setSenderId(UserContextUtils.getId());
            sessionRecord.setReceiverId(talkerUid);
            sessionRecord.setTop(false);
            sessionRecord.setSessionTime(System.currentTimeMillis());
            SessionRecord res = sessionRecordMapper.selectOne(Wrappers
                .<SessionRecord>lambdaQuery()
                .eq(SessionRecord::getSenderId, sessionRecord.getSenderId())
                .eq(SessionRecord::getReceiverId, sessionRecord.getReceiverId()));
            if (res == null) {
                sessionRecordMapper.insert(sessionRecord);
            }
        }
    }

    /**
     * 删除指定记录
     */
    @Override
    public void removeRecord(Long receiverId) {
        sessionRecordMapper.delete(Wrappers
            .<SessionRecord>lambdaQuery()
            .eq(SessionRecord::getSenderId,UserContextUtils.getId())
            .eq(SessionRecord::getReceiverId,receiverId));
    }

    /**
     * 批量拼接数据
     */
    private List<SessionRecordVo> copyVOBatch(List<SessionRecord> sessionRecords) {
        List<SessionRecordVo> sessionRecordVoList = new ArrayList<>();
        for (SessionRecord sessionRecord : sessionRecords) {
            SessionRecordVo sessionRecordVo = copyVo(sessionRecord);
            sessionRecordVoList.add(sessionRecordVo);
        }
        return sessionRecordVoList;
    }

    /**
     * 拼接数据
     */
    private SessionRecordVo copyVo(SessionRecord sessionRecord) {
        SessionRecordVo sessionRecordVo = new SessionRecordVo();
        BeanUtils.copyProperties(sessionRecord, sessionRecordVo);

        // 最后一条消息
        WhisperForm form = new WhisperForm();
        form.setSessionId(sessionRecord.getReceiverId());
        PageParams params = new PageParams();
        params.setPageNum(1);
        params.setPageSize(1);
        MessageVo vo = messageService.getWhisperPage(form, params).getRecords().get(0);
        sessionRecordVo.setLastMsg(vo);

        return sessionRecordVo;
    }

}




