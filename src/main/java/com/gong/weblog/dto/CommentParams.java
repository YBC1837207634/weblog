package com.gong.weblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentParams extends PageParams{

    private Long articleId;

    private boolean getNew = false;

}
