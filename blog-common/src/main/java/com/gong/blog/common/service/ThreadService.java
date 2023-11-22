package com.gong.blog.common.service;

import com.gong.blog.common.entity.Article;
import com.gong.blog.common.mapper.ArticleMapper;

public interface ThreadService {
    public void updateViewCount(ArticleMapper articleMapper, Article article, Integer retryCount);
}
