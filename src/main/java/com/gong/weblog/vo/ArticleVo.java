package com.gong.weblog.vo;

import com.gong.weblog.entity.Article;
import com.gong.weblog.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class ArticleController extends Article {

    private static final long serialVersionUID = 1L;

    private List<Tag> Tags;



}
