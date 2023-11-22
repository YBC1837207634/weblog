package com.gong.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.common.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author asus
* @description 针对表【article_tag】的数据库操作Mapper
* @createDate 2023-10-20 10:03:17
* @Entity generator.entity.ArticleTag
*/
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    List<Long> selectHotTagIds(Integer tagCount);

}




