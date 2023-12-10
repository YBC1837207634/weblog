package com.gong.blog.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.core.constants.MessageType;
import com.gong.blog.core.dto.MessageDTO;
import com.gong.blog.core.entity.Message;
import com.gong.blog.core.form.WhisperForm;
import com.gong.blog.core.mapper.MessageMapper;
import com.gong.blog.core.service.MessageService;
import com.gong.blog.core.vo.MessageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author asus
* @description 针对表【message】的数据库操作Service实现
* @createDate 2023-11-30 19:00:47
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 将接受的文本消息转换为Message
     */
    @Override
    public Message parserTextMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        message.setSendTime(System.currentTimeMillis());
        message.setMsgStatus("1");
        message.setMsgType(MessageType.TEXT);
        return message;
    }

    /**
     * 获取聊天记录
     * @param form
     * @param params
     * @return
     */
    @Override
    public IPage<MessageVo> getWhisperPage(WhisperForm form, PageParams params) {
        IPage<Message> page = new Page<>(params.getPageNum(), params.getPageSize());
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.nested(w -> w.eq(Message::getSenderId, UserContextUtils.getId())
                            .eq(Message::getReceiverId, form.getSessionId()));
        wrapper.or(w -> w.eq(Message::getSenderId, form.getSessionId())
                            .eq(Message::getReceiverId, UserContextUtils.getId()));
        wrapper.orderByDesc(Message::getSendTime);
        messageMapper.selectPage(page, wrapper);
        List<MessageVo> vos = copyVOs(page.getRecords());
        IPage<MessageVo> voIPage = new Page<>();
        BeanUtils.copyProperties(page, voIPage, "records");
        voIPage.setRecords(vos);
        return voIPage;
    }

    /**
     * 批量
     * @param messages
     * @return
     */
    public List<MessageVo> copyVOs(List<Message> messages) {
        List<MessageVo> vos = new ArrayList<>();
        for (Message message : messages) {
            vos.add(convertVO(message));
        }
        return vos;
    }

    /**
     * 频接数据
     * @param message
     * @return
     */
    public MessageVo convertVO(Message message) {
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyProperties(message, messageVo);
        return messageVo;
    }

}




