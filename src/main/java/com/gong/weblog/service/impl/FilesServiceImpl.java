package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.entity.Files;
import com.gong.weblog.service.FilesService;
import com.gong.weblog.mapper.FilesMapper;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【files】的数据库操作Service实现
* @createDate 2023-10-27 15:17:40
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
    implements FilesService{

}




