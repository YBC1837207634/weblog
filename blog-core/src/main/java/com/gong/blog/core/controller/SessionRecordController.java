package com.gong.blog.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.vo.Result;
import com.gong.blog.core.service.SessionRecordService;
import com.gong.blog.core.vo.SessionRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/session/record")
@Api(tags = {"会话记录"})
public class SessionRecordController {


    @Autowired
    private SessionRecordService sessionRecordService;

    /**
     * 获取会话记录列表
     * @param params
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "获取会话记录列表")
    public Result<IPage<SessionRecordVo>> list(PageParams params) {
        return Result.success(sessionRecordService.getRecordList(params));
    }

    /**
     * 创建会话记录
     * @param talkerUid
     * @return
     */
    @GetMapping("getSession")
    @ApiOperation(value = "获取会话")
    public Result<String> createSession(Long talkerUid) {
        sessionRecordService.recordSession(talkerUid);
        return Result.success("ok");
    }

    /**
     * 删除会话记录
     * @param talkerUid
     * @return
     */
    @DeleteMapping("delSession")
    @ApiOperation(value = "删除会话记录")
    public Result<String> rm(Long talkerUid) {
        sessionRecordService.removeRecord(talkerUid);
        return  Result.success("ok");
    }

}
