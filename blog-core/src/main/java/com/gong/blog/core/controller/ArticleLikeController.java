package com.gong.blog.core.controller;

import com.gong.blog.common.common.ActionType;
import com.gong.blog.common.form.RelationForm;
import com.gong.blog.common.service.ArticleUserLikeService;
import com.gong.blog.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article/like")
public class ArticleLikeController {

    @Autowired
    private ArticleUserLikeService articleUserLikeService;

    @PostMapping
    public Result<String> like(@RequestBody RelationForm form) {
        if (form.getAct().equals(ActionType.ADD)) {
            articleUserLikeService.like(form.getGoalId());
        } else {
            articleUserLikeService.cancel(form.getGoalId());
        }
        return Result.success("操作成功");
    }

    @GetMapping("/query/{id}")
    public Result<String> isLike(@PathVariable("id") Long id) {
        if (articleUserLikeService.isLike(id)) {
            return Result.success("ok");
        }
        return Result.success("no");
    }

}
