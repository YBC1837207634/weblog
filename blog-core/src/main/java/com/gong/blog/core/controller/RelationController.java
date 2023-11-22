package com.gong.blog.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.form.RelationForm;
import com.gong.blog.common.params.RelationParams;
import com.gong.blog.common.service.RelationService;
import com.gong.blog.common.vo.Result;
import com.gong.blog.common.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "关注")
@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    /**
     * 关注
     * @param form
     * @return
     */
    @PostMapping("/modify")
    @ApiOperation(value = "关注", notes = "")
    @Log(title = "关注", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> relation(@RequestBody @Validated RelationForm form) {
        relationService.link(form);
        return Result.success("操作成功");
    }

    @GetMapping("/list")
    @ApiOperation(value = "根据关注者id获取", notes = "")
    public Result<IPage<UserVo>> page(RelationParams params) {
        return Result.success(relationService.getGoalPage(params));
    }


    /**
     * 查询当前用户是否关注某用户
     * @param id
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "是否关注", notes = "")
    public Result<IPage<UserVo>> page(Long id) {
        if (relationService.queryRelation(id)) {
            return Result.success("ok");
        }
        return Result.success("no");

    }

}
