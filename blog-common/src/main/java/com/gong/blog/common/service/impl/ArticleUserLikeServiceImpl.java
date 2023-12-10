package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.ArticleUserLike;
import com.gong.blog.common.exception.NotHaveDataException;
import com.gong.blog.common.mapper.ArticleMapper;
import com.gong.blog.common.mapper.ArticleUserLikeMapper;
import com.gong.blog.common.service.ArticleUserLikeService;
import com.gong.blog.common.utils.UserContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


/**
* @author asus
* @description 针对表【article_user_like】的数据库操作Service实现
* @createDate 2023-11-22 11:05:22
*/
@Service
public class ArticleUserLikeServiceImpl extends ServiceImpl<ArticleUserLikeMapper, ArticleUserLike> implements ArticleUserLikeService{

    @Autowired
    private ArticleUserLikeMapper articleUserLikeMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 点赞
     * @param articleId
     */
    @Override
    public void like(Long articleId) {
        if (articleMapper.selectById(articleId) == null) {
            throw new NotHaveDataException("不存在的数据");
        }
        ArticleUserLike articleUserLike = articleUserLikeMapper.selectOne(
                Wrappers
                        .<ArticleUserLike>lambdaQuery()
                        .eq(ArticleUserLike::getArticleId, articleId)
                        .eq(ArticleUserLike::getUserId, UserContextUtils.getId()));
        // 没有点赞过就添加
        if (articleUserLike == null) {
            ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
            ArticleUserLike a = new ArticleUserLike();
            a.setArticleId(articleId);
            a.setUserId(UserContextUtils.getId());
            articleUserLikeMapper.insert(a);
            zSetOperations.incrementScore("article_record:like_count", articleId.toString(), 1);
        }
    }

    /**
     * 取消点赞
     * @param articleId
     */
    @Override
    public void cancel(Long articleId) {
        LambdaQueryWrapper<ArticleUserLike> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleUserLike::getArticleId, articleId);
        lambdaQueryWrapper.eq(ArticleUserLike::getUserId, UserContextUtils.getId());
        int delete = articleUserLikeMapper.delete(lambdaQueryWrapper);
        if (delete > 0) {
            ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.incrementScore("article_record:like_count", articleId.toString(), -1);
        }
    }

    /**
     * 是否点赞
     * @param articleId
     * @return
     */
    public boolean isLike(Long articleId) {
        return articleUserLikeMapper.selectOne(
                Wrappers
                    .<ArticleUserLike>lambdaQuery()
                    .eq(ArticleUserLike::getArticleId, articleId)
                    .eq(ArticleUserLike::getUserId, UserContextUtils.getId())
        ) != null;
    }


//    /**
//     * 点赞
//     * @param articleId
//     */
//    @Override
//    public void like(Long articleId) {
//        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//        String res = hashOperations.get("article:relation", articleId + "_" + UserContextUtils.getId());
//        if (res == null) {
//            if (articleMapper.selectById(articleId) == null)
//                throw new NotHaveDataException("不存在的数据");
//            hashOperations.put("article:relation", articleId + "_" + UserContextUtils.getId(), "1");
//        }
//        if (StringUtils.hasText(res) && res.equals("0")) {
//            hashOperations.increment("article_record:like_count", articleId.toString(), 1L);
//        }
//    }
//
//    /**
//     * 取消点赞
//     * @param articleId
//     */
//    @Override
//    public void cancel(Long articleId) {
//        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//        String res = hashOperations.get("article:relation", articleId + "_" + UserContextUtils.getId());
//        if (res != null && res.equals("1")) {
//            hashOperations.put("article:relation", articleId + "_" + UserContextUtils.getId(), "0");
//            hashOperations.increment("article_record:like_count", articleId.toString(), -1L);
//        }
//    }
}




