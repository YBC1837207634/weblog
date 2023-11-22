package com.gong.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.common.entity.Article;
import com.gong.blog.common.entity.Collect;
import com.gong.blog.common.params.CollectParams;

import java.util.List;

/**
* @author asus
* @description 针对表【collect】的数据库操作Mapper
* @createDate 2023-11-01 10:32:28
* @Entity com.gong.blog.common.entity.Collect
*/
public interface CollectMapper extends BaseMapper<Collect> {
    List<Article> selectArticleByAffiliationId(CollectParams params);
}




