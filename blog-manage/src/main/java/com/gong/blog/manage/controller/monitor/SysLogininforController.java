package com.gong.blog.manage.controller.monitor;

import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.entity.SysLogininfor;
import com.gong.blog.manage.service.SysLogininforService;
import com.gong.blog.manage.vo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitor/recordLogin")
public class SysLogininforController {

    private SysLogininforService sysLogininforService;

    @Autowired
    public SysLogininforController(SysLogininforService sysLogininforService) {
        this.sysLogininforService = sysLogininforService;
    }

    /**
     * 查询
     */
    @GetMapping("/list")
    Result<Pages<SysLogininfor>> list(SysLogininfor sysLogininfor) {
        List<SysLogininfor> list = sysLogininforService.getList(sysLogininfor);
        return Result.success(Pages.createPage(list));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @Log(title = "删除登陆日志", businessType = BusinessType.DELETE)
    Result<String> remove(@PathVariable List<Long> ids) {
        sysLogininforService.removeByIds(ids);
        return Result.success("删除成功");
    }

    @DeleteMapping
    @Log(title = "删除登陆日志", businessType = BusinessType.DELETE)
    Result<String> removeAll() {
        sysLogininforService.removeByIds(null);
        return Result.success("删除成功");
    }
}
