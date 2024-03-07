package com.gong.blog.common.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.*;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.exception.NotHaveDataException;
import com.gong.blog.common.exception.ParamException;
import com.gong.blog.common.form.ArticleForm;
import com.gong.blog.common.mapper.*;
import com.gong.blog.common.params.ArticleParams;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.service.ArticleTagService;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.common.vo.ArticleVo;
import com.gong.blog.common.vo.TagVo;
import com.gong.blog.common.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @author asus
* @description 针对表【article】的数据库操作Service实现
* @createDate 2023-10-20 10:00:46
*/
@Service
@CacheConfig(cacheNames = "articleLimit")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

//    @Autowired
//    private ThreadService threadService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ArticleUserLikeMapper articleUserLikeMapper;

    /**
     * 获取文章详情，查看量加一
     * @param id
     * @return
     */
    @Override
//    @Cacheable(value = "article", key = "'articleContent_' + #id")
    public ArticleVo getArticleContent(Long id) {
        Article article = articleMapper.selectById(id);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        if (Objects.isNull(article)) throw new NotHaveDataException("请求数据不存在");
        // 如果查看的文章是非公共开的并且要查看的文章的用户不是作者本人
        if (article.getCommon().equals(0) && !UserContextUtils.getId().equals(article.getAuthorId())) {
            throw new ParamException("不可查看非公开内容");
        }

        // 更新阅读量
//        Article temp = new Article();
//        temp.setId(article.getId());
//        temp.setViewCounts(article.getViewCounts());
//        threadService.updateViewCount(articleMapper, temp, 2);
        hashOperations.increment("article_record:view_count", id.toString(), 1);
        ArticleVo articleVo = extraInfo(article, true, true); // 添加标签
        ArticleContent articleContent;
        if (UserContextUtils.getId().equals(1L) && article.getAnonymous().equals(0)) {
            articleContent = new ArticleContent();
            articleContent.setArticleId(id);
            articleContent.setContent("<p>登录查看详细内容</p>");
        } else {
            articleContent = articleContentMapper.selectById(article.getBodyId());
            if (Objects.isNull(articleContent)) throw new NotHaveDataException("请求数据不存在");
        }
        articleVo.setContent(articleContent);
        return articleVo;
    }

    /**
     * 获取文章基本信息
     */
    @Override
//    @Cacheable(value = "articleLimit", key = "#params.toString()")
    public IPage<ArticleVo> getArticleVoPage(ArticleParams params) {
        IPage<Article> page = new Page<>(params.getPageNum(), params.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "1");
        queryWrapper.eq(params.getAuthorId() != null, "author_id", params.getAuthorId());
        // 类别
        queryWrapper.eq(params.getCategoryId()!=null, "category_id", params.getCategoryId());
        // common
        if (params.getAuthorId() == null || !params.getAuthorId().equals(UserContextUtils.getId())) {
            queryWrapper.eq("common", 1);
        }
        String sortField = getSortField(params.getSortField());
        queryWrapper.orderBy(sortField !=null,!params.getSort().equals("desc"), sortField);
        articleMapper.selectPage(page, queryWrapper);
        return extraInfoForEach(page, true, true);
    }

    /**
     * 根据标签id列表获取文章
     * @param params
     * @return
     */
//    @Cacheable(value = "articlePageByTags", key = "#params.toString()", unless = "#result.records.empty")
    public IPage<ArticleVo> getArticleVoByTags(ArticleParams params) {
        IPage<Article> page = new Page<>(params.getPageNum(), params.getPageSize());
        params.setSortField(getSortField(params.getSortField()));
        params.setSort("desc");
        IPage<Article> result = articleMapper.selectArticleByTags(page, params, false);
        return extraInfoForEach(result, true, true);
    }

    /**
     * 根据字段获取排名，走缓存
     * @return
     */
    @Override
    public List<ArticleVo> getArticleVoRank(int num) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        List<ArticleVo> articleList = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> article = zSetOperations.reverseRangeByScoreWithScores("articleRank:limit" + num, 0, Double.MAX_VALUE, 0, num);
        // 从缓存中查询是否有数据
        if (!article.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> item : article) {
                articleList.add(JSON.parseObject(item.getValue(), ArticleVo.class));
            }
            return articleList;
        } else {
            // 根据分数从大到小返回 num 个
            Set<String> res = zSetOperations.reverseRangeByScore("article_record:like_count", 0, Double.MAX_VALUE, 0, num);
            if (res != null && !res.isEmpty()) {
                // 根据id获取文章
                List<Long> ids = res.stream().map(Long::valueOf).toList();
                for (Long id : ids) {
                    ArticleVo vo = extraInfo(articleMapper.selectById(id), true, true);;
                    String member = JSON.toJSONString(vo);
                    long score = Objects.requireNonNullElse(
                                zSetOperations.score("article_record:like_count", id.toString()),
                                0
                            ).longValue();
                    // 存放到缓存中
                    zSetOperations.add("articleRank:limit" + num, member, score);
                    redisTemplate.expire("articleRank:limit" + num, 60, TimeUnit.SECONDS);
                    articleList.add(vo);
                }
                return articleList;
            }
        }
        return articleList;
    }

    @Override
    public List<ArticleVo> getArticleVoRank2() {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String articleHotRank = opsForValue.get("articleHostRank");
        return JSON.parseArray(articleHotRank, ArticleVo.class);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "articleLimit", allEntries = true)
    public boolean saveArticle(ArticleForm articleForm) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        // 保存文章内容
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent(articleForm.getContent());
        articleContentMapper.insert(articleContent);
        // 保存文章信息
        Article article = new Article();
        BeanUtils.copyProperties(articleForm, article);
        article.setAuthorId(UserContextUtils.getId());
        article.setBodyId(articleContent.getId());
        article.setWeight(0);
        article.setImg(articleForm.getImg());
        article.setLikeCount(0);
        article.setViewCounts(0);
        article.setCollectCount(0);
        article.setAnonymous(articleForm.isAnonymous()? 1 : 0);
        if (UserContextUtils.isAdmin()) {
            article.setStatus("1");
        } else {
            article.setStatus("1");  // 审核
        }
        // 类别
        if (tagMapper.selectById(articleForm.getCategory()) == null) {
            article.setCategoryId(1L);
        } else {
            article.setCategoryId(articleForm.getCategory());
        }
        int insert = articleMapper.insert(article);
        // 标签
        List<ArticleTag> tags = new ArrayList<>();
        for (long id : articleForm.getTagIds()) {
            ArticleTag temp = new ArticleTag();
            temp.setTagId(id);
            temp.setArticleId(article.getId());
            tags.add(temp);
        }
        articleTagService.saveBatch(tags);
        // 给文章内容设置文章id
        ArticleContent a = new ArticleContent();
        a.setId(articleContent.getId());
        a.setArticleId(article.getId());
        articleContentMapper.updateById(a);
        // 浏览量
        hashOperations.put("article_record:view_count", article.getId().toString(),"0");
        // 收藏量
        hashOperations.put("article_record:collect_count", article.getId().toString(), "0");
        // 点赞
//        hashOperations.put("article_record:like_count", article.getId().toString(), "0");
        zSetOperations.add("article_record:like_count", article.getId().toString(), 0);
        // 评论
        hashOperations.put("article_record:comment_count", article.getId().toString(), "0");
        return insert != 0;
    }

    /**
     * 搜索文章
     * @return
     */
    @Override
    public IPage<ArticleVo> searchArticleVoPage(ArticleParams params) {
        IPage<Article> page = new Page<>(params.getPageNum(), params.getPageSize());
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = Wrappers.<Article>lambdaQuery()
            .like(StringUtils.hasText(params.getKeyword()), Article::getTitle, params.getKeyword())
            .eq(Article::getCommon, 1)
            .orderByDesc(Article::getViewCounts)
            .orderByDesc(Article::getCreateTime);
        articleMapper.selectPage(page, articleLambdaQueryWrapper);
        return extraInfoForEach(page, true, true);
    }

    /**
     * 遍历文章列表给文章添加标签
     *
     * @return 文章+标签
     */
    public IPage<ArticleVo> extraInfoForEach(IPage<Article> articleList, boolean tag, boolean author) {
        IPage<ArticleVo> page = new Page<>();
        List<ArticleVo> articleVos = new ArrayList<>();
        BeanUtils.copyProperties(articleList, page, "records");
        if (tag || author) {
            for (Article article: articleList.getRecords()) {
                ArticleVo articleVo = extraInfo(article, tag, author);
                articleVos.add(articleVo);
            }
        }
        return page.setRecords(articleVos);
    }

    public List<ArticleVo> extraInfoForEach(List<Article> articleList, boolean tag, boolean author) {
        List<ArticleVo> articleVos = new ArrayList<>();
        if (tag || author) {
            for (Article article: articleList) {
                // 添加标签作者信息
                articleVos.add(extraInfo(article, tag, author));
            }
        }
        return articleVos;
    }

    /**
     * 给文章添加额外信息, 不包括文章体
     * @param article
     * @param tag
     * @param author
     * @return
     */
    public ArticleVo extraInfo(Article article, boolean tag, boolean author) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        // 标签
        if (tag) {
            articleVo.setTags(getTagsFromCache(articleVo.getId(), hashOperations));
        }
        // 添加作者信息
        if (author) {
            UserVo userVoByCache = userService.getUserVoByCache(articleVo.getAuthorId(), hashOperations);
            articleVo.setAuthor(userVoByCache);
        }
        // 设置类别
        setCategoryFromCache(articleVo, hashOperations);
        Tag tag1 = tagMapper.selectById(article.getCategoryId());
        if (tag1 != null) {
            articleVo.setCategory(tag1.getTagName());
        }

        setCount(articleVo, hashOperations, zSetOperations);

        return articleVo;
    }

    private void setCategoryFromCache(ArticleVo articleVo, HashOperations<String, String, String> hashOperations) {
        TagVo tagVo = getTagsFromCache(articleVo.getId(), hashOperations).get(0);
        if (tagVo != null)
            articleVo.setCategory(tagVo.getTagName());
    }

    /**
     * 设置标签信息，从缓存中获取
     */
    private List<TagVo> getTagsFromCache(Long id, HashOperations<String, String, String> hashOperations) {
        String res = hashOperations.get("articleExtraInfo:tag", id.toString());
        if (StringUtils.hasText(res)) {
            return JSON.parseArray(res, TagVo.class);
        } else {
            List<Tag> tags = articleMapper.selectArticleTagByArticleId(id);
            List<TagVo> tagVoList = new ArrayList<>();
            for (Tag tag : tags) {
                TagVo vo = new TagVo();
                BeanUtils.copyProperties(tag, vo);
                tagVoList.add(vo);
            }
            if (!tags.isEmpty()) {
                hashOperations.put("articleExtraInfo:tag", id.toString(), JSON.toJSONString(tagVoList));
                redisTemplate.expire("articleExtraInfo:tag", 120, TimeUnit.SECONDS);
                return tagVoList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 更新文章
     * @param articleForm
     * @return
     */
    @Override
    @Transactional
//    @CacheEvict(value = "article", key = "'articleContent_' + #articleForm.articleIds")
    public void updateArticle(ArticleForm articleForm) {
        Article article = new Article();
        BeanUtils.copyProperties(articleForm, article);
        article.setCategoryId(articleForm.getCategory());
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        // 更新文章信息
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAuthorId, UserContextUtils.getId()); // 只可以更新当前用户的文章
        queryWrapper.eq(Article::getId, articleForm.getArticleId());
        if (articleMapper.update(article, queryWrapper) == 0) {
            throw new CUDException("更新失败");
        }
        // 更新标签
        if (!articleForm.getTagIds().isEmpty()) {
            articleTagService.remove(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, articleForm.getArticleId()));
            List<ArticleTag> articleTags = new ArrayList<>();
            for (Long id : articleForm.getTagIds()) {
                ArticleTag at = new ArticleTag();
                at.setArticleId(articleForm.getArticleId());
                at.setTagId(id);
                articleTags.add(at);
            }
            articleTagService.saveBatch(articleTags);
        }
        // 查询文章体的id
        Article res = articleMapper.selectById(articleForm.getArticleId());
        // 更新文章内容
        ArticleContent articleContent = new ArticleContent();
//        BeanUtils.copyProperties(articleForm, articleContent);
        articleContent.setContent(articleForm.getContent());
        articleContent.setId(res.getBodyId());
        if (articleContentMapper.updateById(articleContent) == 0) {
            throw new CUDException("更新失败");
        }
        hashOperations.delete("articleExtraInfo:tag", articleForm.getArticleId().toString());
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"articleLimit", "article"}, allEntries = true)
    public void removeArticle(List<Long> ids) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        queryWrapper.eq(Article::getAuthorId, UserContextUtils.getId()); // 只可删除自己的文章
        queryWrapper.in(!ids.isEmpty(), Article::getId, ids);
        int delete = articleMapper.delete(queryWrapper);
        // 删除文章体
        LambdaQueryWrapper<ArticleContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArticleContent::getArticleId, ids);
        int delete1 = articleContentMapper.delete(wrapper);
        if (delete != delete1) {
            throw new CUDException("删除失败！");
        }
        // 收藏夹
        LambdaQueryWrapper<Collect> collectQuery = new LambdaQueryWrapper<>();
        collectQuery.in(Collect::getItemId, ids);
        collectMapper.delete(collectQuery);
        // 删除文章所对应的评论
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.in(Comment::getArticleId, ids);
        commentMapper.delete(commentQuery);
        // 删除绑定标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.in(ArticleTag::getArticleId, ids);
        articleTagService.remove(articleTagLambdaQueryWrapper);
        List<String> articleIds = ids.stream().map(Object::toString).toList();
        // 删除点赞
        LambdaQueryWrapper<ArticleUserLike> articleUserLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleUserLikeLambdaQueryWrapper.in(ArticleUserLike::getArticleId, ids);
        articleUserLikeMapper.delete(articleUserLikeLambdaQueryWrapper);
        // 浏览量
        hashOperations.delete("article_record:view_count", articleIds.toArray());
        // 收藏量
        hashOperations.delete("article_record:collect_count", articleIds.toArray());
        // 点赞
        zSetOperations.remove("article_record:like_count", articleIds.toArray());
        // 评论
        hashOperations.delete("article_record:comment_count", articleIds.toArray());
        // 标签缓存
        hashOperations.delete("articleExtraInfo:tag", articleIds.toArray());
        // 删除作者缓存
        hashOperations.delete("userVo", UserContextUtils.getId().toString());
        // 删除文章用户点赞关系
//        Long userId = UserContextUtils.getId();
//        ScanOptions.ScanOptionsBuilder scanOptionsBuilder = ScanOptions.scanOptions();
//        for (String articleId : articleIds) {
//            Cursor<Map.Entry<String, String>> article =
//                    hashOperations.scan("article:relation", scanOptionsBuilder.match(articleId + "*").build());
//            article.stream().toList().forEach(item -> {
//                hashOperations.delete("article:relation", item.getKey() + ":" + userId);
//            });
//
//        }

    }

    /**
     * 批量设置文章的相关数值信息
     */
    @Override
    public void setCountBatch(List<ArticleVo> list) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        for (ArticleVo articleVo : list) {
            setCount(articleVo, hashOperations, zSetOperations);
        }
    }

    public void setCount(ArticleVo articleVo, HashOperations<String, String, String> hashOperations, ZSetOperations<String, String> zSetOperations) {
        // 评论数量
        String commentCount = hashOperations.get("article_record:comment_count", articleVo.getId().toString());
        articleVo.setCommentCounts(Integer.valueOf(Objects.requireNonNullElse(commentCount, "0")));

        // 收藏数量
//        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        collectLambdaQueryWrapper.eq(Collect::getItemId, articleVo.getId());
//        Integer count = collectMapper.selectCount(collectLambdaQueryWrapper).intValue();
//        articleVo.setCollectCount(count);
        String collectCount = hashOperations.get("article_record:collect_count", articleVo.getId().toString());
        articleVo.setCollectCount(Integer.valueOf(Objects.requireNonNullElse(collectCount, "0")));

        // 浏览量
        String viewCount = hashOperations.get("article_record:view_count", articleVo.getId().toString());
        articleVo.setViewCounts(Integer.valueOf(Objects.requireNonNullElse(viewCount, "0")));

        // 点赞
        int likeCount = Objects.requireNonNullElse(zSetOperations.score("article_record:like_count", articleVo.getId().toString()), 0).intValue();
        articleVo.setLikeCount(likeCount);
    }

    private String getSortField(String field) {
        if (StringUtils.hasText(field)) {
            if (field.equals("createTime") || field.equals("create_time")) {
                return "create_time";
            } else if (field.equals("view_counts") || field.equals("viewCounts")) {
                return "view_counts";
            }
        }
        return null;
    }

}




