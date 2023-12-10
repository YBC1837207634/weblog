package com.gong.blog.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.core.dto.MessageDTO;
import com.gong.blog.core.entity.Message;
import com.gong.blog.core.form.WhisperForm;
import com.gong.blog.core.vo.MessageVo;

import java.util.List;

/**
* @author asus
* @description 针对表【message】的数据库操作Service
* @createDate 2023-11-30 19:00:47
*/
public interface MessageService extends IService<Message> {
    Message parserTextMessage(MessageDTO messageDTO);

    List<MessageVo> copyVOs(List<Message> messages);

    MessageVo convertVO(Message message);

    IPage<MessageVo> getWhisperPage(WhisperForm form, PageParams params);
}
