package com.gong.blog.manage.mapper;

import com.gong.blog.manage.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {

    int insertBatch(List<SysUserRole> sysUserRoleList);

    int insertOne(SysUserRole sysUserRole);

    int deleteByUserId(Long userId);

    int deleteByRoleId(Long roleId);

    int deleteByRoleIds(List<Long> roleIds);

    int deleteByUserIds(List<Long> userIds);

    List<Long> selectRoleIdByUserId(Long userId);

    List<SysUserRole> selectList(SysUserRole sysUserRole);

    int updateByUserId(SysUserRole sysUserRole);
}
