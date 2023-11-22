package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.ArticleTag;
import com.gong.blog.common.mapper.ArticleTagMapper;
import com.gong.blog.common.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【article_tag】的数据库操作Service实现
* @createDate 2023-10-20 10:03:17
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService {

}




