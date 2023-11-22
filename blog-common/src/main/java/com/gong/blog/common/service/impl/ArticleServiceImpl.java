package com.gong.blog.common.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        ArticleContent articleContent;
        articleContent = articleContentMapper.selectById(article.getBodyId());
        if (Objects.isNull(articleContent)) throw new NotHaveDataException("请求数据不存在");
        // 更新阅读量
//        Article temp = new Article();
//        temp.setId(article.getId());
//        temp.setViewCounts(article.getViewCounts());
//        threadService.updateViewCount(articleMapper, temp, 2);
        hashOperations.increment("article_record:view_count", id.toString(), 1);
        ArticleVo articleVo = extraInfo(article, true, true); // 添加标签
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
     * 根据字段获取排名
     * @param field
     * @return
     */
    @Override
    public List<ArticleVo> getArticleVoByRank(String field) {
        if (field.equals("collect_count")) {

        }
        return null;
    }

    @Override
    @Transactional
//    @CacheEvict(value = "articleLimit", allEntries = true)
    public boolean saveArticle(ArticleForm articleForm) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
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
        hashOperations.put("article_record:like_count", article.getId().toString(), "0");
        // 评论
        hashOperations.put("article_record:comment_count", article.getId().toString(), "0");
        return insert != 0;
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
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        // 标签
        if (tag) {
            List<TagVo> tags = articleMapper.selectArticleTagByArticleId(article.getId());
            if (!tags.isEmpty()) {
                articleVo.setTags(tags);
            }
        }
        // 添加作者信息
        if (author) {
            UserVo userVo = userService.getUserVo(article.getAuthorId());
            articleVo.setAuthor(userVo);
        }
        // 设置类别
        Tag tag1 = tagMapper.selectById(article.getCategoryId());
        if (tag1 != null) {
            articleVo.setCategory(tag1.getTagName());
        }

        // 评论数量
        String commentCount = hashOperations.get("article_record:comment_count", article.getId().toString());
        articleVo.setCommentCounts(Integer.valueOf(Objects.requireNonNullElse(commentCount, "0")));

        // 收藏数量
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.eq(Collect::getItemId, article.getId());
        Integer count = collectMapper.selectCount(collectLambdaQueryWrapper).intValue();
        articleVo.setCollectCount(count);

        // 浏览量
        String viewCount = hashOperations.get("article_record:view_count", article.getId().toString());
        articleVo.setViewCounts(Integer.valueOf(Objects.requireNonNullElse(viewCount, "0")));

        // 点赞
        String likeCount = hashOperations.get("article_record:like_count", article.getId().toString());
        articleVo.setLikeCount(Integer.valueOf(Objects.requireNonNullElse(likeCount, "0")));

        return articleVo;
    }

    /**
     * 更新文章
     * @param articleForm
     * @return
     */
    @Override
    @Transactional
//    @CacheEvict(value = "article", key = "'articleContent_' + #articleForm.articleId")
    public void updateArticle(ArticleForm articleForm) {
        Article article = new Article();
        BeanUtils.copyProperties(articleForm, article);
        article.setCategoryId(articleForm.getCategory());
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
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"articleLimit", "article"}, allEntries = true)
    public void removeArticle(List<Long> ids) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
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
        List<String> strings = ids.stream().map(Object::toString).toList();
        // 删除点赞
        LambdaQueryWrapper<ArticleUserLike> articleUserLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleUserLikeLambdaQueryWrapper.in(ArticleUserLike::getArticleId, ids);
        articleUserLikeMapper.delete(articleUserLikeLambdaQueryWrapper);
        // 浏览量
        hashOperations.delete("article_record:view_count", strings.toArray());
        // 收藏量
        hashOperations.delete("article_record:collect_count", strings.toArray());
        // 点赞
        hashOperations.delete("article_record:like_count", strings.toArray());
        // 评论
        hashOperations.delete("article_record:comment_count", strings.toArray());
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




