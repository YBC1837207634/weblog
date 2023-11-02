package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.entity.WeblogLogininfor;
import com.gong.weblog.mapper.WeblogLogininforMapper;
import com.gong.weblog.service.WeblogLogininforService;
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




