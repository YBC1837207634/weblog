package com.gong.blog.manage.controller.weblog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gong.blog.common.entity.Tag;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.TagService;
import com.gong.blog.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("weblog/tag")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 新增
     */
    @PostMapping
    Result<String> save(@RequestBody Tag tag) {
        tagService.save(tag);
        return Result.success("添加成功");
    }

    /**
     * 修改
     */
    @PutMapping
    Result<String> update(@RequestBody Tag tag) {
        tagService.updateById(tag);
        return Result.success("更新成功");
    }

    /**
     * 查询
     */
    @GetMapping("/list")
    Result<IPage<Tag>> list(PageParams params) {
        IPage<Tag> page = new Page<>(params.getPageNum(), params.getPageSize());
        tagService.page(page);
        return Result.success(page);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    Result<String> remove(@PathVariable List<Long> ids) {
        tagService.removeByIds(ids);
        return Result.success("删除成功");
    }
}
