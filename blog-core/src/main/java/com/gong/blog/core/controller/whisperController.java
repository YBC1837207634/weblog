package com.gong.blog.core.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.vo.Result;
import com.gong.blog.core.form.WhisperForm;
import com.gong.blog.core.service.MessageService;
import com.gong.blog.core.vo.MessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/whisper")
@Api(tags = {"私信"})
public class whisperController {

    @Autowired
    private MessageService messageService;

    @GetMapping("list")
    @ApiOperation(value = "获取聊天记录")
    public Result<IPage<MessageVo>> list(WhisperForm form, PageParams params) {
        return Result.success(messageService.getWhisperPage(form, params));
    }

}
