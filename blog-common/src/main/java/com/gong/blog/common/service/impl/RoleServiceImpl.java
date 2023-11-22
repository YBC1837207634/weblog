package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.Role;
import com.gong.blog.common.mapper.RoleMapper;
import com.gong.blog.common.service.RoleService;
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




