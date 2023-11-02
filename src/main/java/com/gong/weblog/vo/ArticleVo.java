package com.gong.weblog.vo;

import com.gong.weblog.entity.Article;
import com.gong.weblog.entity.ArticleContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class ArticleVo extends Article {

    private static final long serialVersionUID = 1L;

    // 文章内容
    private ArticleContent content;

    private UserVo author;

    private List<TagVo> Tags;

    private String category;

}
