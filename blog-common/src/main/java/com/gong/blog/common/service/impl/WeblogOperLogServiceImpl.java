package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.WeblogOperLog;
import com.gong.blog.common.mapper.WeblogOperLogMapper;
import com.gong.blog.common.service.WeblogOperLogService;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【weblog_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2023-10-27 14:02:18
*/
@Service
public class WeblogOperLogServiceImpl extends ServiceImpl<WeblogOperLogMapper, WeblogOperLog>
    implements WeblogOperLogService{

}




