package com.gong.blog.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.Article;
import com.gong.blog.common.form.ArticleForm;
import com.gong.blog.common.params.ArticleParams;
import com.gong.blog.common.vo.ArticleVo;

import java.util.List;

/**
* @author asus
* @description 针对表【article】的数据库操作Service
* @createDate 2023-10-20 10:00:46
*/


public interface ArticleService extends IService<Article> {

    IPage<ArticleVo> getArticleVoPage(ArticleParams params);

    List<ArticleVo> getArticleVoByRank(String field);

    ArticleVo getArticleContent(Long id);

    boolean saveArticle(ArticleForm articleForm);

    IPage<ArticleVo> extraInfoForEach(IPage<Article> articleList, boolean tag, boolean author);

    List<ArticleVo> extraInfoForEach(List<Article> articleList, boolean tag, boolean author);

    ArticleVo extraInfo(Article article, boolean tag, boolean author);

    void updateArticle(ArticleForm articleForm);

    void removeArticle(List<Long> ids);

}
