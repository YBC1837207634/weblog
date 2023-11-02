package com.gong.weblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gong.weblog.dto.PageParams;
import com.gong.weblog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.weblog.vo.CommentVo;

/**
* @author asus
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-10-21 12:44:59
*/
public interface CommentService extends IService<Comment> {
    IPage<CommentVo> getCommnetVoPageByArticleId(Long articleId, PageParams params);
}
