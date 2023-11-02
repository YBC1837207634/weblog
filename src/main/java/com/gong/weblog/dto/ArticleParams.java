package com.gong.weblog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleParams extends PageParams {

    private List<Long> tags;

    private Long authorId;
}
