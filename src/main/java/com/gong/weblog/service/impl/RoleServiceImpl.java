package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.entity.Role;
import com.gong.weblog.mapper.RoleMapper;
import com.gong.weblog.service.RoleService;
import org.springframework.stereotype.Service;

/**
* @author asus
* @description 针对表【role】的数据库操作Service实现
* @createDate 2023-10-29 13:15:10
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




