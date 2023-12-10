package com.gong.blog.core.service;

import com.gong.blog.common.entity.Article;
import com.gong.blog.common.mapper.ArticleMapper;
import com.gong.blog.core.entity.SessionRecord;
import com.gong.blog.core.mapper.SessionRecordMapper;

public interface ThreadService {
    void updateViewCount(ArticleMapper articleMapper, Article article, Integer retryCount);

    void updateSessionTime(SessionRecordMapper sessionRecordMapper, SessionRecord sessionRecord);

}
