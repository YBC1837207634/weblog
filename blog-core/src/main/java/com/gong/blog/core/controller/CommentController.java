package com.gong.blog.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.form.CommentForm;
import com.gong.blog.common.params.CommentParams;
import com.gong.blog.common.service.CommentService;
import com.gong.blog.common.vo.CommentVo;
import com.gong.blog.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评论")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "分页查询某个文章下的评论", notes = "")
    @GetMapping("/list")
    @Log(title = "评论查询", businessType = BusinessType.SELECT, onlyError = true)
    public Result<IPage<CommentVo>> list(@Validated CommentParams params) {
        return Result.success(commentService.getCommentVoPageByArticleId(params));
    }


    @ApiOperation(value = "发表评论", notes = "")
    @PostMapping("/push")
    @Log(title = "发表评论", businessType = BusinessType.INSERT, onlyError = true)
    public Result<String> pushComment(@RequestBody @Validated CommentForm commentForm) {
        if (commentService.pushComment(commentForm)) {
            return Result.success("评论成功！");
        }
        return Result.error(ResponseStatus.INTERNAL, "评论失败");
    }



}
