package com.gong.blog.manage.service;

import com.gong.blog.manage.entity.SysUserRole;

import java.util.List;


public interface SysUserRoleService {

    List<SysUserRole> getList(SysUserRole sysUserRole);

    int saveOne(SysUserRole sysUserRole);

    int saveBatch(List<SysUserRole> sysUserRoles);

    int updateSysUserRoleByUserId(SysUserRole sysUserRole);

    int removeByUserId(Long userId);

    int removeByUserIds(List<Long> userIds);

}
