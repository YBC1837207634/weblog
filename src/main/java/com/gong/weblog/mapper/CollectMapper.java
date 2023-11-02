package com.gong.weblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.weblog.dto.CollectParams;
import com.gong.weblog.entity.Article;
import com.gong.weblog.entity.Collect;

import java.util.List;

/**
* @author asus
* @description 针对表【collect】的数据库操作Mapper
* @createDate 2023-11-01 10:32:28
* @Entity com.gong.weblog.entity.Collect
*/
public interface CollectMapper extends BaseMapper<Collect> {
    List<Article> selectArticleByAffiliationId(CollectParams params);
}




