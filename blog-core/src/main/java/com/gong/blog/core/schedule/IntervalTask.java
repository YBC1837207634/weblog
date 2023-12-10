package com.gong.blog.core.schedule;

import com.alibaba.fastjson2.JSON;
import com.gong.blog.common.constants.ArticleWeight;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@EnableScheduling //开启定时任务
@Component
public class IntervalTask {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;


    /**
     * 更新热文章
     */
    @Scheduled(cron = "0 0/60 * * * ?")
    public void updateHotArticle() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        List<ArticleVo> list = articleService.list()
                .stream()
                .map(article -> articleService.extraInfo(article, true, true))
                .toList();
        for (ArticleVo vo : list) {
            String commentCount = hashOperations.get("article_record:comment_count", vo.getId().toString());
            String viewCount = hashOperations.get("article_record:view_count", vo.getId().toString());
            int likeCount = zSetOperations.score("article_record:like_count", vo.getId().toString()).intValue();
            String collectCount = hashOperations.get("article_record:collect_count", vo.getId().toString());
            vo.setViewCounts(Integer.valueOf(viewCount));
            vo.setCommentCounts(Integer.valueOf(commentCount));
            vo.setLikeCount(likeCount);
            vo.setCollectCount(Integer.valueOf(collectCount));
            // 设置得分
            vo.setScore(computedScore(vo));
        }
        // 排序 取前30个存入redis
        List<ArticleVo> articleVos = list.stream().sorted(Comparator.comparing(ArticleVo::getScore).reversed()).toList();
        if (articleVos.size() > 30) {
            articleVos = articleVos.subList(0, 30);
        }
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("articleHostRank", JSON.toJSONString(articleVos));
    }


    /**
     * 计算分值
     * @return
     */
    private long computedScore(ArticleVo articleVo) {
        long score = 0;
        long likeCount = (long) articleVo.getLikeCount();
        long viewCount = (long) articleVo.getViewCounts();
        long collectCount = (long) articleVo.getCollectCount();
        long commentCount = (long) articleVo.getCommentCounts();
        if (likeCount != 0) {
            score +=  likeCount * ArticleWeight.LIKE_COUNT_WEIGHT;
        }
        if (viewCount != 0) {
            score += viewCount * ArticleWeight.VIEW_COUNT_WEIGHT;
        }
        if (collectCount != 0) {
            score += collectCount * ArticleWeight.COLLECT_COUNT_WEIGHT;
        }
        if (commentCount != 0) {
            score += commentCount * ArticleWeight.COMMENT_COUNT_WEIGHT;
        }
        return score;
    }

}
