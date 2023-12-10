package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.constants.CommentType;
import com.gong.blog.common.entity.Comment;
import com.gong.blog.common.form.CommentForm;
import com.gong.blog.common.mapper.ArticleMapper;
import com.gong.blog.common.mapper.CommentMapper;
import com.gong.blog.common.params.CommentParams;
import com.gong.blog.common.service.CommentService;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.common.vo.CommentVo;
import com.gong.blog.common.vo.SubComment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    private ArticleMapper articleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 通过文章id来获取对应评论
     */
    @Override
    @Cacheable(value = "articleCommentLimit", key = "#params.toString()")
    public IPage<CommentVo> getCommentVoPageByArticleId(CommentParams params) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, params.getArticleId());
        queryWrapper.eq(Comment::getCommentType, CommentType.COMMENT);
        if (params.isGetNew()) {
            queryWrapper.orderByDesc(Comment::getCreateTime);
        } else {
            queryWrapper.orderByDesc(Comment::getLikeCount);
        }
        IPage<Comment> page = new Page<>(params.getPageNum(), params.getPageSize());
        IPage<Comment> pageResult = commentMapper.selectPage(page, queryWrapper);
        // 拼接数据
        return combine(pageResult);
    }

    /**
     * 发表评论
     * @param commentForm
     * @return
     */
    @Override
    @CacheEvict(value = "articleCommentLimit", allEntries = true)
    public boolean pushComment(CommentForm commentForm) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentForm, comment);
        comment.setAuthorId(UserContextUtils.getId());

        if (commentForm.getCommentType().equals(CommentType.COMMENT)) {
            comment.setToId(0L);
            comment.setParentId(0L);
            if (!checked(comment, true,  false, false))
                return false;
            // 留言
        } else if (commentForm.getCommentType().equals(CommentType.SUB_COMMENT)) {
            comment.setToId(0L);
//            comment.setArticleId(0L);
            if (!checked(comment, true,  true, false))
                return false;
            // 回复
        } else if (commentForm.getCommentType().equals(CommentType.REPLY)) {
//            comment.setArticleId(0L);
            if (!checked(comment, true,  true, true))
                return false;
        }
        try {
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            commentMapper.insert(comment);
            // 更新文章对应的评论数
            hashOperations.increment("article_record:comment_count", comment.getArticleId().toString(), 1L);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean checked(Comment comment, boolean checkArt, boolean checkParent, boolean toId) {
        if (checkArt) {
            if (Objects.isNull(articleMapper.selectById(comment.getArticleId())))
                return false; // 检查是否有文章
        }
        if (checkParent) {
            Comment c = commentMapper.selectById(comment.getParentId());
            if (Objects.isNull(c) || !c.getCommentType().equals(CommentType.COMMENT))
                return false;
        }
        if (toId) {
            return !Objects.isNull(userService.getById(comment.getToId()));
        }
        return true;
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
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment , commentVo);
        // 评论者的具体信息
        commentVo.setCommentator(userService.getUserVoByCache(comment.getAuthorId(), hashOperations));
        // 获取子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Comment::getCreateTime);  // 时间正序
        queryWrapper.eq(Comment::getParentId, comment.getId());
        List<Comment> children = commentMapper.selectList(queryWrapper);
        List<SubComment> subComment = getSubComment(children);
        // 是否含有子评论
        if (!subComment.isEmpty())
            commentVo.setSubComments(subComment);
        return commentVo;
    }

    /**
     * 根据评论列表获取子评论
     * @param commentList
     * @return
     */
    private List<SubComment> getSubComment(List<Comment> commentList) {
        List<SubComment> subComments = new ArrayList<>();
        for(Comment comment : commentList) {
            subComments.add(getTypeSubComment(comment));
        }
        return subComments;
    }

    /**
     * 返回子评论，根据条件返回不同的 SubComment
     * @param comment
     * @return
     */
    private SubComment getTypeSubComment(Comment comment) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        SubComment c = new SubComment();
        // 设置留言内容
        c.setContent(comment.getContent());
        // 设置留言者
        c.setCommentator(userService.getUserVoByCache(comment.getAuthorId(), hashOperations));
        c.setType(CommentType.COMMENT);
        // 如果是回复类型
        if (comment.getCommentType().equals(CommentType.REPLY)) {
            // 回复目标
            c.setTarget(userService.getUserVoByCache(comment.getToId(), hashOperations));
            c.setType(CommentType.REPLY);
        }
        // 公共字段
        c.setCreateTime(comment.getCreateTime());
        c.setLikeCount(comment.getLikeCount());
        c.setTrampleCount(comment.getTrampleCount());
        c.setId(comment.getId());
        return c;
    }

}




