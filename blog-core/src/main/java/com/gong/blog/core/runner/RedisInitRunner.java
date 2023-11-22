package com.gong.blog.core.runner;

import com.gong.blog.common.entity.Article;
import com.gong.blog.common.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    public void run(String... args) {
        List<Article> list = articleService.list();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        for (Article a : list) {
            // 浏览量
            hashOperations.put("article_record:view_count", a.getId().toString(), a.getViewCounts().toString());
            // 收藏量
//            hashOperations.put("article_record:collect_count", a.getId().toString(), a.getCollectCount().toString());
            // 点赞
            hashOperations.put("article_record:like_count", a.getId().toString(), a.getLikeCount().toString());
            // 评论
            hashOperations.put("article_record:comment_count", a.getId().toString(), a.getCommentCounts().toString());
        }
//        System.out.println(hashOperations.entries("article_record"));
    }
}
