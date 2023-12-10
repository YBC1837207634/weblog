package com.gong.blog.core.runner;

import com.gong.blog.common.entity.Article;
import com.gong.blog.common.entity.ArticleUserLike;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.service.ArticleUserLikeService;
import com.gong.blog.core.schedule.IntervalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 初始化redis数据
 */
@Component
public class RedisInitRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleUserLikeService articleUserLikeService;

    @Autowired
    private IntervalTask intervalTask;

    @Override
    public void run(String... args) {
        List<Article> list = articleService.list();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        for (Article a : list) {
            // 浏览量
            hashOperations.put("article_record:view_count", a.getId().toString(), a.getViewCounts().toString());
            // 点赞
            zSetOperations.add("article_record:like_count", a.getId().toString(), a.getLikeCount());
            // 评论
            hashOperations.put("article_record:comment_count", a.getId().toString(), a.getCommentCounts().toString());
            // 收藏
            hashOperations.put("article_record:collect_count", a.getId().toString(), a.getCollectCount().toString());
        }
        intervalTask.updateHotArticle();
    }


    private void initRelation(HashOperations<String, String, String> hashOperations) {
        List<ArticleUserLike> list = articleUserLikeService.list();
        for (ArticleUserLike a : list) {
            hashOperations.put("article:relation", a.getArticleId() + "_" + a.getUserId(), "1");
        }
    }
}
