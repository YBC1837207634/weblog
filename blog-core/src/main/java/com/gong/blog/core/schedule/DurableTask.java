package com.gong.blog.core.schedule;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gong.blog.common.entity.Article;
import com.gong.blog.common.entity.ArticleTag;
import com.gong.blog.common.entity.Tag;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.service.ArticleTagService;
import com.gong.blog.common.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@EnableScheduling //开启定时任务
@Component
public class DurableTask {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 将redis中的数据固化到本地数据库
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void saveMysql() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Map<String, String> viewCount = hashOperations.entries("article_record:view_count");
        List<Article> articleList = new ArrayList<>();
        Set<String> ids = viewCount.keySet();
        for (String id : ids) {
            Article article = new Article(
                    Long.valueOf(id),
                    Integer.valueOf(Objects.requireNonNullElse(hashOperations.get("article_record:comment_count", id), "0")),
                    Integer.valueOf(viewCount.get(id)),
                    Objects.requireNonNullElse(zSetOperations.score("article_record:like_count", id), 0).intValue(),
                    Integer.valueOf(Objects.requireNonNullElse(hashOperations.get("article_record:collect_count", id), "0")
            ));
            articleList.add(article);
        }

        articleService.updateBatchById(articleList);
    }

    @Scheduled(cron = "0 0/8 * * * ?")
    private void updateTag() {
        // 更新标签
        for (Tag tag : tagService.list()) {
            long count = articleTagService.count(
                Wrappers
                    .<ArticleTag>lambdaQuery()
                    .eq(ArticleTag::getTagId, tag.getId()));
            tag.setArticleCount((int) count);
            tagService.updateById(tag);
        }
    }

    /**
     * 持久化关注数据
     * @param hashOperations
     */
//    private void saveRelation(HashOperations<String, String, String> hashOperations) {
//        Map<String, String> entries = hashOperations.entries("article:relation");
//        entries.forEach((key, value) -> {
//            String[] split = key.split(":");
//            if (value.equals("1")) {
//                ArticleUserLike a = new ArticleUserLike();
//                a.setArticleId(Long.valueOf(split[0]));
//                a.setUserId(Long.valueOf(split[1]));
//                articleUserLikeService.save(a);
//            } else {
//                LambdaQueryWrapper<ArticleUserLike> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(ArticleUserLike::getArticleId, Long.valueOf(split[0]));
//                lambdaQueryWrapper.eq(ArticleUserLike::getUserId, Long.valueOf(split[1]));
//                articleUserLikeService.remove(lambdaQueryWrapper);
//            }
//        });
//    }

}
