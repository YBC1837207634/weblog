package com.gong.blog.manage.controller.weblog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.entity.WeblogOperLog;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.WeblogOperLogService;
import com.gong.blog.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weblog/log/operateLog")
public class WeblogOperLogController {

    private WeblogOperLogService weblogOperLogService;

    @Autowired
    public WeblogOperLogController(WeblogOperLogService weblogOperLogService) {
        this.weblogOperLogService = weblogOperLogService;
    }

    /**
     * 查询
     */
    @GetMapping("/list")
    Result<IPage<WeblogOperLog>> list(PageParams params) {
        IPage<WeblogOperLog> page = new Page<>(params.getPageNum(), params.getPageSize());
        weblogOperLogService.page(page);
        return Result.success(page);
    }

    /**
     * 删除
     */
    @Log(title = "删除操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    Result<String> remove(@PathVariable List<Long> ids) {
        weblogOperLogService.removeByIds(ids);
        return Result.success("删除成功");
    }
}
