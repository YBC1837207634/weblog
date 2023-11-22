package com.gong.blog.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.TagService;
import com.gong.blog.common.vo.Result;
import com.gong.blog.common.vo.TagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "标签")
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

//    @ApiOperation(value = "热门标签", notes = "默认5个")
//    @GetMapping("/hot")
//    public Result<List<Tag>> hot (@RequestParam(defaultValue = "5") Integer count) {
//        return Result.success(tagService.getHotTags(count));
//    }

    @ApiOperation(value = "用于首页展示的标签列表", notes = "")
    @GetMapping("/category")
    public Result<IPage<TagVo>> page (PageParams params) {
        return Result.success(tagService.getCategory(params));
    }

    @ApiOperation(value = "所有标签分页", notes = "")
    @GetMapping("/list")
    public Result<IPage<TagVo>> list(PageParams params) {
        return Result.success(tagService.getPage(params));
    }

}
