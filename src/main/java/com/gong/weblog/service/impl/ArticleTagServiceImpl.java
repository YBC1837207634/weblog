package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.entity.ArticleTag;
import generator.service.ArticleTagService;
import generator.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【article_tag】的数据库操作Service实现
* @createDate 2023-10-20 10:03:17
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




