package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.entity.ArticleContent;
import com.gong.weblog.mapper.ArticleContentMapper;
import com.gong.weblog.service.ArticleContentService;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【article_content】的数据库操作Service实现
* @createDate 2023-10-20 15:51:16
*/
@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent>
    implements ArticleContentService {

}




