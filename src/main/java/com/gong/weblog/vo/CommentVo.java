package com.gong.weblog.vo;


import com.gong.weblog.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentVo extends Comment {

    private static final long serialVersionUID = 1L;


    private UserVo commentator;

    // 二级评论回复
    private List<SubComment> subComments;


}
