package com.gong.weblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.weblog.dto.ArticleForm;
import com.gong.weblog.dto.ArticleParams;
import com.gong.weblog.entity.Article;
import com.gong.weblog.vo.ArticleVo;

import java.util.List;

/**
* @author asus
* @description 针对表【article】的数据库操作Service
* @createDate 2023-10-20 10:00:46
*/
public interface ArticleService extends IService<Article> {

    IPage<ArticleVo> getArticleVoPage(ArticleParams params);


    ArticleVo getArticleContent(Long id);


    boolean saveArticle(ArticleForm articleForm);

    IPage<ArticleVo> extraInfoForEach(IPage<Article> articleList, boolean tag, boolean author);

    List<ArticleVo> extraInfoForEach(List<Article> articleList, boolean tag, boolean author);

    ArticleVo extraInfo(Article article, boolean tag, boolean author);

    void updateArticle(ArticleForm articleForm);

    void removeArticle(List<Long> ids);

}
