package com.gong.blog.manage.controller.weblog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.entity.WeblogLogininfor;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.WeblogLogininforService;
import com.gong.blog.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weblog/log/recordLogin")
public class WeblogLogininforController {

    private WeblogLogininforService weblogLogininforService;

    @Autowired
    public WeblogLogininforController(WeblogLogininforService weblogLogininforService) {
        this.weblogLogininforService = weblogLogininforService;
    }


    /**
     * 查询
     */
    @GetMapping("/list")
    Result<IPage<WeblogLogininfor>> list(PageParams params) {
        IPage<WeblogLogininfor> page = new Page<>(params.getPageNum(), params.getPageSize());
        weblogLogininforService.page(page);
        return Result.success(page);
    }

    /**
     * 删除
     */
    @Log(title = "删除登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    Result<String> remove(@PathVariable List<Long> ids) {
        weblogLogininforService.removeByIds(ids);
        return Result.success("删除成功");
    }
}
