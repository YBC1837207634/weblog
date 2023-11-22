package com.gong.blog.manage.service;

import com.gong.blog.manage.dto.BaseUserInfo;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysUser;

import java.util.List;
import java.util.Set;


public interface SysUserService {

    SysUser getById(Long id);

    List<SysUser> getList(SysUser params);

    public int count();

    public List<SysRole> getSysRoleByUserId(Long userId);

    int saveUserAssignRole(SysUserDTO user);

    int saveOne(SysUser user);

    int saveBatch(List<SysUser> sysUsers);

    int updateSysUserById(SysUser sysUser);

    int updateBaseUserInfo(BaseUserInfo userInfo);

    void updatePwd(SysUser user);

    void updateUserRoleById(BaseUserInfo userInfo);

    int removeById(Long id);

    int removeByIds(List<Long> ids);

    public List<String> getPurviewByUserId(long id);

    public void assignmentRole(long userId, Set<Long> roleIds);

}
