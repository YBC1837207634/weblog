package com.gong.blog.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.form.ArticleForm;
import com.gong.blog.common.params.ArticleParams;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.vo.ArticleVo;
import com.gong.blog.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "文章")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "查询分页列表", notes = "")
    @GetMapping("/list")
    public Result<IPage<ArticleVo>> list(@Validated ArticleParams params) {
        if (params.getTags() == null || params.getTags().isEmpty()) {
            return Result.success(articleService.getArticleVoPage(params));
        } else {
            return Result.success(articleService.getArticleVoByTags(params));
        }

    }

    @ApiOperation(value = "文章详情", notes = "")
    @GetMapping("/content/{id}")
    public Result<ArticleVo> articleContent(@PathVariable("id") Long id) {
        return Result.success(articleService.getArticleContent(id));
    }

    @ApiOperation(value = "发布文章", notes = "")
    @PostMapping
    @Log(title = "发布文章", businessType = BusinessType.INSERT, onlyError = true)
    public Result<String> save(@RequestBody @Validated ArticleForm articleForm) {
        if (articleService.saveArticle(articleForm)) {
            return Result.success("发布成功！");
        }
        return Result.success("发布文章失败！");
    }

    @ApiOperation(value = "更新文章", notes = "")
    @Log(title = "更新文章", businessType = BusinessType.EDIT, onlyError = true)
    @PutMapping
    public Result<String> update(@RequestBody @Validated ArticleForm articleForm) {
        articleService.updateArticle(articleForm);
        return Result.success("更新成功");
    }


    @ApiOperation(value = "删除文章", notes = "")
    @Log(title = "删除文章", businessType = BusinessType.DELETE, onlyError = true)
    @DeleteMapping("/{ids}")
    public Result<String> remove(@PathVariable("ids") List<Long> ids) {
        articleService.removeArticle(ids);
        return Result.success("删除成功！");
    }

//    @ApiOperation(value = "获取收藏", notes = "")
//    @PostMapping("/rank")
//    public Result<List<ArticleVo>> getCollect(String field) {
//        return Result.success(articleService.getArticleVoByRank(field));
//    }

    @ApiOperation(value = "搜索", notes = "")
    @GetMapping("/search")
    public Result<IPage<ArticleVo>> search(ArticleParams params) {
        return Result.success(articleService.searchArticleVoPage(params));
    }

}
