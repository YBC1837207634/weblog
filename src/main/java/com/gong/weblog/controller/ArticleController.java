package com.gong.weblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.weblog.annotation.Log;
import com.gong.weblog.dto.ArticleForm;
import com.gong.weblog.dto.ArticleParams;
import com.gong.weblog.enums.BusinessType;
import com.gong.weblog.service.ArticleService;
import com.gong.weblog.vo.ArticleVo;
import com.gong.weblog.vo.Result;
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
        return Result.success(articleService.getArticleVoPage(params));
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

    @ApiOperation(value = "获取收藏", notes = "")
    @PostMapping("/rank")
    public Result<List<ArticleVo>> getCollect(String field) {
        return Result.success(articleService.getArticleVoByRank(field));
    }

}
