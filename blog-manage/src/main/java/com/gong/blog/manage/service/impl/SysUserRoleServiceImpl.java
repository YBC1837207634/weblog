package com.gong.blog.manage.service.impl;

import com.gong.blog.manage.entity.SysUserRole;
import com.gong.blog.manage.mapper.SysUserRoleMapper;
import com.gong.blog.manage.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysUserRole> getList(SysUserRole sysUserRole) {
        return sysUserRoleMapper.selectList(sysUserRole);
    }

    @Override
    public int saveOne(SysUserRole sysUserRole) {
        return sysUserRoleMapper.insertOne(sysUserRole);
    }

    @Override
    public int saveBatch(List<SysUserRole> sysUserRole) {
        return sysUserRoleMapper.insertBatch(sysUserRole);
    }

    @Override
    public int updateSysUserRoleByUserId(SysUserRole sysUserRole) {
        return sysUserRoleMapper.updateByUserId(sysUserRole);
    }

    @Override
    public int removeByUserId(Long userId) {
        return sysUserRoleMapper.deleteByUserId(userId);
    }

    @Override
    public int removeByUserIds(List<Long> userIds) {
        return sysUserRoleMapper.deleteByUserIds(userIds);
    }
}
