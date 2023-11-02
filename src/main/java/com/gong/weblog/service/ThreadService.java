package com.gong.weblog.service;

import com.gong.weblog.entity.Article;
import com.gong.weblog.mapper.ArticleMapper;

public interface ThreadService {
    public void updateViewCount(ArticleMapper articleMapper, Article article, Integer retryCount);
}
