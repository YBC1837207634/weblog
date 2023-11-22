package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.WeblogLogininfor;
import com.gong.blog.common.mapper.WeblogLogininforMapper;
import com.gong.blog.common.service.WeblogLogininforService;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【weblog_logininfor(系统访问记录)】的数据库操作Service实现
* @createDate 2023-10-27 14:02:18
*/
@Service
public class WeblogLogininforServiceImpl extends ServiceImpl<WeblogLogininforMapper, WeblogLogininfor>
    implements WeblogLogininforService{

}




