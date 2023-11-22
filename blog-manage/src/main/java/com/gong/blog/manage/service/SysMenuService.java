package com.gong.blog.manage.service;

import com.gong.blog.manage.entity.SysMenu;
import com.gong.blog.manage.vo.MenuItem;
import com.gong.blog.manage.vo.Route;

import java.util.List;


public interface SysMenuService {

    SysMenu getById(Long id);

    List<SysMenu> getList(SysMenu sysMenu);

    int saveOne(SysMenu menu);

    int saveBatch(List<SysMenu> sysMenus);

    int updateSysMenuById(SysMenu menu);

    int removeById(Long id);

    List<Route> getRouter(Long userId);

    List<Route> getChildrenList(List<SysMenu> menus, long parent);

    List<SysMenu> getMenuListByRoleId(long roleId);

    List<MenuItem> getMenuTree();

    List<MenuItem> menuTree(List<SysMenu> menus, long parent);

    void setDefault(SysMenu menu);

}
