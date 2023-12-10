package com.gong.blog.core.controller;

import com.gong.blog.common.entity.User;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.vo.ArticleVo;
import com.gong.blog.common.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reveal")
@Api(tags = {"展示榜单"})
public class RevealController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/user/rank/{num}")
    public Result<List<User>> userRank(@PathVariable(value = "num") Integer num) {
        return Result.success(userService.getUserRank(num));
    }

    @GetMapping("/article/rank/{num}")
    public Result<List<ArticleVo>> articleRank(@PathVariable(value = "num") Integer num) {
        return Result.success(articleService.getArticleVoRank(num));
    }

    @GetMapping("/article/rank")
    public Result<List<ArticleVo>> articleRank2() {
        return Result.success(articleService.getArticleVoRank2());
    }


}
