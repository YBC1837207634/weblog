package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.entity.Article;
import generator.service.ArticleService;
import generator.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【article】的数据库操作Service实现
* @createDate 2023-10-20 10:00:46
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




