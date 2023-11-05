package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.dto.ArticleForm;
import com.gong.weblog.dto.ArticleParams;
import com.gong.weblog.entity.*;
import com.gong.weblog.exception.CUDException;
import com.gong.weblog.exception.NotHaveDataException;
import com.gong.weblog.exception.ParamException;
import com.gong.weblog.mapper.*;
import com.gong.weblog.service.ArticleService;
import com.gong.weblog.service.ArticleTagService;
import com.gong.weblog.service.ThreadService;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.ArticleVo;
import com.gong.weblog.vo.TagVo;
import com.gong.weblog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private ThreadService threadService;

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


    /**
     * 获取文章详情，查看量加一
     * @param id
     * @return
     */
    @Override
    public ArticleVo getArticleContent(Long id) {
        Article article = articleMapper.selectById(id);
        if (Objects.isNull(article)) throw new NotHaveDataException("请求数据不存在");
        // 如果查看的文章是非公共开的并且要查看的文章的用户不是作者本人
        if (article.getCommon().equals(0) && (UserContextUtils.isAnonymous() || !UserContextUtils.getId().equals(article.getAuthorId()))) {
            throw new ParamException("不可查看非公开内容");
        }
        ArticleContent articleContent;
        if (UserContextUtils.isAnonymous() && article.getAnonymous().equals(0)) {
            articleContent = new ArticleContent();
            articleContent.setContent("<h4>登录解锁所有内容</h4>");
        } else {
            articleContent = articleContentMapper.selectById(article.getBodyId());
            if (Objects.isNull(articleContent)) throw new NotHaveDataException("请求数据不存在");
            // 更新阅读量
            Article temp = new Article();
            temp.setId(article.getId());
            temp.setViewCounts(article.getViewCounts());
            threadService.updateViewCount(articleMapper, temp, 2);
        }
        ArticleVo articleVo = extraInfo(article, true, true); // 添加标签
        articleVo.setContent(articleContent);
        return articleVo;
    }

    /**
     * 获取文章基本信息
     */
    @Override
    public IPage<ArticleVo> getArticleVoPage(ArticleParams params) {
        String field = getSortField(params.getSortField());
        // 匿名用户只可以访问前20条
        if (UserContextUtils.isAnonymous() ) {
            params.setPageNum(1);
            params.setPageSize(20);
        }
        // 查询条件
        ArticleParams p = new ArticleParams();
        p.setAuthorId(params.getAuthorId());
        if (Objects.nonNull(field)) {
            p.setSortField(field);
        }
        if (!Objects.isNull(params.getTags()) && !params.getTags().isEmpty()) {
            IPage<Article> page = new Page<>();
            if (params.getSort().equals("desc")) {
                p.setSort("desc");
            } else {
                p.setSort("asc");
            }
            p.setTags(params.getTags());
            // 多表查询
            p.setPageNum((params.getPageNum() - 1) * params.getPageSize());
            p.setPageSize(params.getPageSize() * params.getPageNum());
            List<Article> articles;
            // 提供用户id 并且查询当前用户的文章列表时，可以查看所有文章
            if (params.getAuthorId() != null && params.getAuthorId().equals(UserContextUtils.getId())) {
                articles = articleMapper.selectArticleByTags(p, true);
            } else {
                // 没有提供用户id，只可以查看公开的文章
                articles = articleMapper.selectArticleByTags(p, false);
            }
//        Long count = articleMapper.selectArticleCountByTags(params);
            page.setRecords(articles);
//        page.setTotal(count);
            page.setSize(articles.size());
            return extraInfoForEach(page, true, true);
            // 不使用标签查询
        } else {
            IPage<Article> page = new Page<>(params.getPageNum(), params.getPageSize());
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", "1");
            queryWrapper.eq(params.getAuthorId() != null, "author_id", params.getAuthorId());
            // common
            if (params.getAuthorId() == null || !params.getAuthorId().equals(UserContextUtils.getId())) {
                queryWrapper.eq("common", 1);
            }
            queryWrapper.orderBy(p.getSortField() !=null,!params.getSort().equals("desc"), p.getSortField());
            articleMapper.selectPage(page, queryWrapper);
            return extraInfoForEach(page, true, true);
        }
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
    public boolean saveArticle(ArticleForm articleForm) {
        // 保存文章内容
        ArticleContent articleContent = new ArticleContent();
//        BeanUtils.copyProperties(articleForm, articleContent);
        articleContent.setContent(articleForm.getContent());
        articleContentMapper.insert(articleContent);
        // 保存文章信息
        Article article = new Article();
        BeanUtils.copyProperties(articleForm, article);
        article.setAuthorId(UserContextUtils.getId());
        article.setBodyId(articleContent.getId());
        article.setWeight(0);
        article.setImg(articleForm.getImg());
        article.setLikeCount(0L);
        article.setViewCounts(0);
        article.setCollectCount(0L);
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
                // 添加标签作者信息
                articleVos.add(extraInfo(article, tag, author));
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
        // 评论数量
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, article.getId());
        articleVo.setCommentCounts(commentMapper.selectCount(queryWrapper));
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
        // 收藏数量
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.eq(Collect::getItemId, article.getId());
        Long count = collectMapper.selectCount(collectLambdaQueryWrapper);
        articleVo.setCollectCount(count);
        return articleVo;
    }

    /**
     * 更新文章
     * @param articleForm
     * @return
     */
    @Override
    @Transactional
    public void updateArticle(ArticleForm articleForm) {
        Article article = new Article();
        BeanUtils.copyProperties(articleForm, article);
        // 更新文章信息
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAuthorId, UserContextUtils.getId()); // 只可以更新当前用户的文章
        queryWrapper.eq(Article::getId, articleForm.getArticleId());
        if (articleMapper.update(article, queryWrapper) == 0) {
            throw new CUDException("更新失败");
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
    public void removeArticle(List<Long> ids) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
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




