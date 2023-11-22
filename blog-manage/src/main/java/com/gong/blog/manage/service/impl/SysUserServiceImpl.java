package com.gong.blog.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.exception.ExistException;
import com.gong.blog.manage.dto.BaseUserInfo;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.entity.SysMenu;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysUser;
import com.gong.blog.manage.entity.SysUserRole;
import com.gong.blog.manage.mapper.SysMenuMapper;
import com.gong.blog.manage.mapper.SysUserMapper;
import com.gong.blog.manage.mapper.SysUserRoleMapper;
import com.gong.blog.manage.service.SysUserService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysUser> getList(SysUser params) {
        if (Objects.isNull(params))
            params = new SysUser();
        if (Objects.nonNull(params.getPage()) && Objects.nonNull(params.getPageSize()))
            PageHelper.startPage(params.getPage(), params.getPageSize());
        List<SysUser> sysUsers = sysUserMapper.selectList(params);
        // 如果不是管理员就过滤掉管理员用户
        if (!CustomUserDetailsUtils.isAdmin()) {
            sysUsers = sysUsers.stream().filter(user -> !user.isAdmin()).toList();
        }
        return sysUsers;
    }

    /**
     * 用户数量
     * @return
     */
    @Override
    public int count() {
        return sysUserMapper.count();
    }


    /**
     * 通过用户id 获取该用户所分配的角色
     * @param userId id
     * @return List<SysRole>
     */
    @Override
    public List<SysRole> getSysRoleByUserId(Long userId) {
        return sysUserMapper.selectRoleByUserId(userId);
    }

    /**
     * 添加一个用户并赋予用户角色
     */
    @Override
    @Transactional
    public int saveUserAssignRole(SysUserDTO user) {
        // 用户信息
        int i = saveOne(user);
        // 分配角色F
        Set<Long> s = new HashSet<>(user.getRoleIds());
        if (s.size()>0) {
            assignmentRole(user.getId(), s);
        }
        // 角色信息
        return i;
    }

    /**
     * 添加一个用户
     * @param user 用户
     * @return int
     */
    @Override
    public int saveOne(SysUser user) {
        if (StringUtils.hasText(user.getUsername())
            && StringUtils.hasText(user.getPassword())
            && user.getUsername().length() <= 30
            && user.getPassword().length() <= 30 ) {
            // 查看该用户是否存在
            SysUser res = sysUserMapper.selectByUserName(user.getUsername());
            if (!Objects.isNull(res))
                throw new ExistException("用户已存在");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new CUDException("用户名密码不可为空！");
        }
        user.setCreateBy(CustomUserDetailsUtils.getId());
        user.setUpdateBy(CustomUserDetailsUtils.getId());
        return sysUserMapper.insertOne(user);
    }
    /**
     * 批量添加
     */
    @Override
    public int saveBatch(List<SysUser> sysUser) {
        return sysUserMapper.insertBatch(sysUser);
    }

    /**
     * 根据用户id更新
     */
    @Override
    public int updateSysUserById(SysUser sysUser) {
        if (!CustomUserDetailsUtils.isAdmin() && sysUser.isAdmin()) {
            throw new CUDException("管理员不可修改");
        }
        // 更新用户信息
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setUpdateBy(CustomUserDetailsUtils.getId());
        return sysUserMapper.updateById(sysUser);
    }

    /**
     * 更新用户的基本信息
     * @param userInfo 基本信息不包含创建日期等信息
     * @return
     */
    @Override
    public int updateBaseUserInfo(BaseUserInfo userInfo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userInfo, user);
        return updateSysUserById(user);
    }

    @Override
    public void updatePwd(SysUser user) {
        if (user.getId() != null && StringUtils.hasText(user.getPassword()) &&  user.getPassword().length() <= 30 ) {
            SysUser u = new SysUser();
            u.setId(user.getId());
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            updateSysUserById(u);
        } else {
            throw new CUDException("格式不正确密码修改失败！");
        }

    }

    /**
     * 更新角色并重新分配角色
     */
    @Override
    @Transactional
    public void updateUserRoleById(BaseUserInfo user) {
        // 更新用户信息
        updateBaseUserInfo(user);
        // 分配角色
        assignmentRole(user.getId(), user.getRoleIds());
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional
    public int removeById(Long id) {
        if (SysUser.isAdmin(id)) throw new CUDException("管理员用户不可删除！");
        int i = sysUserMapper.deleteById(id);
        if (i != 0) sysUserRoleMapper.deleteByUserId(id);
        return i;
    }

    /**
     * 删除多个
     */
    @Override
    public int removeByIds(List<Long> ids) {
        for (Long id : ids) {
            if (SysUser.isAdmin(id)) {
                throw new CUDException("管理员用户不可删除！");
            }
        }
        int i = sysUserMapper.deleteByIds(ids);
        if (i != 0) sysUserRoleMapper.deleteByUserIds(ids);
        return i;
    }

    /**
     * 通过用户id获取 权限标识符 列表
     */
    @Override
    public List<String> getPurviewByUserId(long id) {
        // 如果是管理员用户
        if (SysUser.isAdmin(id)) {
            return List.of("*:*:*");
        }
        List<SysMenu> purview = sysMenuMapper.selectMenuByUserIdAndType(id, "B");
        return purview.stream().map(SysMenu::getPurview).toList();
    }

    /**
     * 给一个用户分配角色
     * @param userId 用户id
     * @param roleIds 角色 id列表
     */
    @Override
    public void assignmentRole(long userId, Set<Long> roleIds) {
        if (Objects.nonNull(roleIds) && !roleIds.isEmpty()) {
            sysUserRoleMapper.deleteByUserId(userId);
            List<SysUserRole> list = new ArrayList<>();
            roleIds.forEach(item -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(item);
                list.add(userRole);
            });
            sysUserRoleMapper.insertBatch(list);
        }
    }

}
