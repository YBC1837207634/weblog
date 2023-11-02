package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.entity.WeblogOperLog;
import com.gong.weblog.mapper.WeblogOperLogMapper;
import com.gong.weblog.service.WeblogOperLogService;
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




