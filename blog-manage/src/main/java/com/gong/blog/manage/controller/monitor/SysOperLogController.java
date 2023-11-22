package com.gong.blog.manage.controller.monitor;

import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.entity.SysOperLog;
import com.gong.blog.manage.service.SysOperLogService;
import com.gong.blog.manage.vo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitor/operLog")
public class SysOperLogController {

    private SysOperLogService sysOperLogService;

    @Autowired
    public SysOperLogController(SysOperLogService sysOperLogService) {
        this.sysOperLogService = sysOperLogService;
    }


    /**
     * 查询
     */
    @GetMapping("/list")
    Result<Pages<SysOperLog>> list(SysOperLog sysOperLog) {
        List<SysOperLog> list = sysOperLogService.getList(sysOperLog);
        return Result.success(Pages.createPage(list));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @Log(title = "删除操作日志", businessType = BusinessType.DELETE)
    Result<String> remove(@PathVariable List<Long> ids) {
        sysOperLogService.removeByIds(ids);
        return Result.success("删除成功");
    }

    @DeleteMapping
    @Log(title = "删除操作日志", businessType = BusinessType.DELETE)
    Result<String> removeAll() {
        sysOperLogService.removeByIds(null);
        return Result.success("删除成功");
    }
}
