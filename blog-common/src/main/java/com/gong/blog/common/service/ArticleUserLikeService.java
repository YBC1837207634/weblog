package com.gong.blog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.ArticleUserLike;

/**
* @author asus
* @description 针对表【article_user_like】的数据库操作Service
* @createDate 2023-11-22 11:05:22
*/
public interface ArticleUserLikeService extends IService<ArticleUserLike> {
    void like(Long articleId);

    void cancel(Long articleId);

    boolean isLike(Long articleId);
}
