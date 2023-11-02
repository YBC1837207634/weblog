package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.dto.PageParams;
import com.gong.weblog.entity.Comment;
import com.gong.weblog.entity.User;
import com.gong.weblog.mapper.UserMapper;
import com.gong.weblog.service.CommentService;
import com.gong.weblog.mapper.CommentMapper;
import com.gong.weblog.vo.ArticleVo;
import com.gong.weblog.vo.CommentVo;
import com.gong.weblog.vo.Commentator;
import com.gong.weblog.vo.SubComment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author asus
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-10-21 09:42:14
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 通过文章id来获取对应评论
     * @param articleId
     * @param params
     * @return
     */
    @Override
    public IPage<CommentVo> getCommnetVoPageByArticleId(Long articleId, PageParams params) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        IPage<Comment> page = new Page<>(params.getPageNum(), params.getPageSize());
        IPage<Comment> pageResult = commentMapper.selectPage(page, queryWrapper);
        // 拼接数据

        return null;
    }

    public IPage<CommentVo> combine(IPage<Comment> commentIPage) {
        List<CommentVo> commentVos = new ArrayList<>();
        IPage<CommentVo> newPage = new Page<>();
        BeanUtils.copyProperties(commentIPage, newPage, "records");
        // 给每条评论都携带上额外信息
        for (Comment comment : commentIPage.getRecords()) {
            CommentVo combine = combine(comment);
            commentVos.add(combine);
        }
        newPage.setRecords(commentVos);
        return newPage;
    }

    /**
     * 获取评论的额外信息
     * @param comment
     * @return
     */
    private CommentVo combine(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment , commentVo);
        // 获取评论作者
        User user = userMapper.selectById(comment.getAuthorId());
        if (Objects.nonNull(user)) {
            // 评论者的具体信息
            Commentator commentator = new Commentator();
            commentator.setAuthorName(user.getNickname());
            commentator.setAuthorId(user.getId());
        }
        // 获取子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, comment.getId());
        List<Comment> children = commentMapper.selectList(queryWrapper);
        List<SubComment> subComment = getSubComment(children);
        commentVo.setSubComments(subComment);
        return commentVo;
    }

    private List<SubComment> getSubComment(List<Comment> commentList) {
        
        return null;
    }


}




