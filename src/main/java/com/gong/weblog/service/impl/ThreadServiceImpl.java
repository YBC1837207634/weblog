package com.gong.weblog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gong.weblog.entity.Article;
import com.gong.weblog.mapper.ArticleMapper;
import com.gong.weblog.service.ThreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 被线程池调用
 */
@Slf4j
@Service
public class ThreadServiceImpl implements ThreadService  {


    /**
     * 更新阅读数量
     * @param articleMapper
     * @param article
     * @param retryCount  重试次数
     */
    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper, Article article, Integer retryCount) {
        Long id = article.getId();
        article.setId(null);  // id不可修改
        article.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        queryWrapper.eq(Article::getViewCounts, article.getViewCounts());  // 乐观锁
        int i = articleMapper.update(article, queryWrapper);
        // 重试
        while (i == 0 && retryCount > 0) {
            Article temp = articleMapper.selectById(id);
            Article a = new Article();
            a.setId(temp.getId());
            a.setViewCounts(temp.getViewCounts() + 1);
            int res = articleMapper.updateById(a);
            if (res != 0) break;
            retryCount -= 1;
            log.info("阅读量更新出现问题，重试");
        }

    }


}

