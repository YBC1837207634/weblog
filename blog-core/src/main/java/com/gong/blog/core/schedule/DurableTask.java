package com.gong.blog.core.schedule;


import com.gong.blog.common.entity.Article;
import com.gong.blog.common.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EnableScheduling //开启定时任务
@Component
public class DurableTask {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 将redis中的数据固化到本地数据库
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void saveMysql() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> viewCount = hashOperations.entries("article_record:view_count");
//        Map<String, String> collectCount = hashOperations.entries("article_record:collect_count");
        Map<String, String> likeCount = hashOperations.entries("article_record:like_count");
        Map<String, String> commentCount = hashOperations.entries("article_record:comment_count");
        List<Article> articleList = new ArrayList<>();
        Set<String> ids = viewCount.keySet();
        for (String id : ids) {
            Article article = new Article(
                    Long.valueOf(id),
                    Integer.valueOf(commentCount.get(id)),
                    Integer.valueOf(viewCount.get(id)),
                    Integer.valueOf(likeCount.get(id)),
                    0);
            articleList.add(article);
        }
        articleService.updateBatchById(articleList);
    }

}
