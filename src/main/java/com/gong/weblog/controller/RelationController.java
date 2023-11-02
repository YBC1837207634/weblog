package com.gong.weblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.weblog.annotation.Log;
import com.gong.weblog.dto.PageParams;
import com.gong.weblog.dto.RelationForm;
import com.gong.weblog.enums.BusinessType;
import com.gong.weblog.service.RelationService;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.Result;
import com.gong.weblog.vo.UserVo;
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
    @ApiOperation(value = "关注列表", notes = "")
    public Result<IPage<UserVo>> page(PageParams params) {
        return Result.success(relationService.getGoalPage(UserContextUtils.getId(), params));
    }

}
