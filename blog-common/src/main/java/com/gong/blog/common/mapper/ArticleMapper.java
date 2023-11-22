package com.gong.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.common.entity.Article;
import com.gong.blog.common.params.ArticleParams;
import com.gong.blog.common.vo.TagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author asus
* @description 针对表【article】的数据库操作Mapper
* @createDate 2023-10-20 10:00:46
* @Entity generator.entity.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<TagVo> selectArticleTagByArticleId(Long id);

    List<Article> selectArticleByTags(@Param("params") ArticleParams params, @Param("self") boolean self);

//    Long selectArticleCountByTags(ArticleParams params);
}




