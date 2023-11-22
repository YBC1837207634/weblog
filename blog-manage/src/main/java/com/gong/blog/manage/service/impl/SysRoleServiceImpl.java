package com.gong.blog.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.manage.dto.RoleDTO;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysRoleMenu;
import com.gong.blog.manage.mapper.SysRoleMapper;
import com.gong.blog.manage.mapper.SysRoleMenuMapper;
import com.gong.blog.manage.mapper.SysUserRoleMapper;
import com.gong.blog.manage.service.SysRoleService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysRole getById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public List<SysRole> getList(SysRole sysRole) {
        if (Objects.isNull(sysRole))
            sysRole = new SysRole();
        if (Objects.nonNull(sysRole.getPage()) && Objects.nonNull(sysRole.getPageSize()))
            PageHelper.startPage(sysRole.getPage(), sysRole.getPageSize());
        List<SysRole> list = sysRoleMapper.selectList(sysRole);
        if (!CustomUserDetailsUtils.isAdmin()) {
            list = list.stream().filter(role -> !role.isAdmin()).toList();
        }
        return list;
    }

    /**
     * 保存角色并分配菜单
     * @param roleDTO 角色基本信息和菜单id
     */
    @Override
    @Transactional
    public void saveRoleAndMenu(RoleDTO roleDTO) {
        // 保存角色信息
        saveOne(roleDTO);
        // 保存菜单
        if (Objects.nonNull(roleDTO.getMenuAssignment()) && roleDTO.getMenuAssignment().size() > 0) {
            List<SysRoleMenu> sysRoleMenus = roleDTO.getMenuAssignment().stream().map(item -> {
                SysRoleMenu r = new SysRoleMenu();
                r.setMenuId(item);
                r.setRoleId(roleDTO.getId());
                return r;
            }).toList();
            sysRoleMenuMapper.insertBatch(sysRoleMenus);
        }
    }

    @Override
    public int saveOne(SysRole sysRole) {
        sysRole.setUpdateBy(CustomUserDetailsUtils.getId());
        sysRole.setCreateBy(CustomUserDetailsUtils.getId());
        return sysRoleMapper.insertOne(sysRole);
    }

    @Override
    public int saveBatch(List<SysRole> sysRole) {

        return sysRoleMapper.insertBatch(sysRole);
    }

    @Override
    public int updateSysRoleById(SysRole sysRole) {
        if (!CustomUserDetailsUtils.isAdmin() && sysRole.isAdmin()) {
            throw new CUDException("管理员角色不可修改！");
        }
        sysRole.setUpdateBy(CustomUserDetailsUtils.getId());
        sysRole.setUpdateTime(LocalDateTime.now());
        return sysRoleMapper.updateById(sysRole);
    }

    @Override
    @Transactional
    public int removeByIds(List<Long> ids) {
        for (Long id : ids) {
            if (SysRole.isAdmin(id)) {
                throw new CUDException("管理员用户不可删除！");
            }
        }
        int i = sysRoleMapper.deleteByIds(ids);
        if (i != 0) {
            sysRoleMenuMapper.deleteByRoleIds(ids); // 解除于该角色关联的菜单
            sysUserRoleMapper.deleteByRoleIds(ids);
        }
        return i;
    }

    /**
     * 更新角色信息并且更新角色所分配的菜单
     * @return
     */
    @Override
    @Transactional
    public int updateRoleAndMenu(RoleDTO roleDTO) {
        // 更新角色信息
        int i = updateSysRoleById(roleDTO);
        // 分配菜单
        if (!Objects.isNull(roleDTO.getMenuAssignment()) && !roleDTO.getMenuAssignment().isEmpty()) {
            sysRoleMenuMapper.deleteByRoleId(roleDTO.getId());
            List<SysRoleMenu> list = new ArrayList<>();
            roleDTO.getMenuAssignment().forEach(item -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleDTO.getId());
                roleMenu.setMenuId(item);
                list.add(roleMenu);
            });
            return sysRoleMenuMapper.insertBatch(list);
        }
        return i;
    }
}
