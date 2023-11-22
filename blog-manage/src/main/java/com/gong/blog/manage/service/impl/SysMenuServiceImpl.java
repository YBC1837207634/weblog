package com.gong.blog.manage.service.impl;

import com.gong.blog.common.exception.CUDException;
import com.gong.blog.manage.entity.SysMenu;
import com.gong.blog.manage.mapper.SysMenuMapper;
import com.gong.blog.manage.mapper.SysRoleMenuMapper;
import com.gong.blog.manage.service.SysMenuService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import com.gong.blog.manage.vo.MenuItem;
import com.gong.blog.manage.vo.Meta;
import com.gong.blog.manage.vo.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public SysMenu getById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    @Override
    public List<SysMenu> getList(SysMenu sysMenu) {
        return sysMenuMapper.selectList(sysMenu);
    }

    @Override
    public int saveOne(SysMenu menu) {
        menu.setId(null);
        setDefault(menu);
        menu.setCreateBy(CustomUserDetailsUtils.getId());
        menu.setUpdateBy(CustomUserDetailsUtils.getId());
        return sysMenuMapper.insertOne(menu);
    }

    @Override
    public int saveBatch(List<SysMenu> sysMenu) {
        return sysMenuMapper.insertBatch(sysMenu);
    }

    @Override
    public int updateSysMenuById(SysMenu menu) {
        setDefault(menu);
        menu.setUpdateBy(CustomUserDetailsUtils.getId());
        menu.setUpdateTime(LocalDateTime.now());
        return sysMenuMapper.updateById(menu);
    }

    @Override
    @Transactional
    public int removeById(Long id) {
        SysMenu q = new SysMenu();
        q.setParentId(id);
        // 如果有子菜单就不删除
        if (sysMenuMapper.selectList(q).isEmpty()) {
            int i = sysMenuMapper.deleteById(id);
            if (i == 0) return i;
            sysRoleMenuMapper.deleteByMenuId(id);
            return i;
        }
        throw new CUDException("删除失败！菜单下还有子菜单");
    }

    /**
     * 通过角色id获取所对应的菜单
     * @param roleId 角色id
     * @return 菜单
     */
    @Override
    public List<SysMenu> getMenuListByRoleId(long roleId) {
        return sysMenuMapper.selectMenuByRoleIdType(roleId);
    }

    /**
     * 根据用户的id来获取对应的路由表
     * @param userId id
     * @return List<Route> 路由表
     */
    @Override
    public List<Route> getRouter(Long userId) {
        List<SysMenu> sysMenus;
        // 不提供userId 获取所有可用菜单
        if (userId == null) {
            sysMenus = sysMenuMapper.selectMenuByUserIdAndType(null, "M", "C");
        } else {
            sysMenus = sysMenuMapper.selectMenuByUserIdAndType(userId, "M", "C");
        }
        return getChildrenList(sysMenus, 0);
    }

    /**
     * 传入菜单，递归查找子菜单并返回 Route 列表
     * @param menus 传入菜单列表 传入的菜单不可以是按钮类型
     * @param parentId 父id
     * @return List<Route
     */
    @Override
    public List<Route> getChildrenList(List<SysMenu> menus, long parentId) {
        List<Route> routes = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (!menu.getMenuType().equals("B") && menu.getParentId() == parentId) {
                Route route = new Route();
                // 路由元信息
                Meta meta = new Meta();
                meta.setIcon(menu.getIcon());
                meta.setTitle(menu.getMenuName());
                meta.setCache(menu.getCache());
                meta.setAside(menu.getAside() == 1);
                // 配置路由信息
                route.setName(menu.getName());
                route.setPath(menu.getPath());
                // 设置组件
                if (menu.getParentId() == 0 && menu.getMenuType().equals("M")) {
                    route.setComponent("Layout");
                } else if (menu.getMenuType().equals("M") && menu.getParentId() != 0) {
                    route.setComponent("parentMenu");
                } else if (StringUtils.hasText(menu.getComponent())) {
                    route.setComponent(menu.getComponent());
                }
                // 如果是目录则不可被点击
                if (menu.getMenuType().equals("M")) route.setRedirect("noRedirect");
                route.setMeta(meta);
                // 递归遍历查找是否还有子路由
                List<Route> children = getChildrenList(menus, menu.getId());
                // 如果有就存到当前路由中
                if(children.size() > 0) {
                    route.setChildren(children);
                };
                // 将当前路有存入列表
                routes.add(route);
            }
        }
        return routes;
    }

    /**
     * 返回菜单树
     */
    @Override
    public List<MenuItem> getMenuTree() {
        List<SysMenu> menus = sysMenuMapper.selectList(null);
        List<MenuItem> tree = menuTree(menus, 0);
        return tree;
    }

    /**
     * 生成菜单树 用于前端菜单列表的渲染
     * @param menus 菜单列表
     * @param parent 父id
     * @return List<MenuItem>
     */
    public List<MenuItem> menuTree(List<SysMenu> menus, long parent) {
        List<MenuItem> menuItems = new ArrayList<>();
        for (SysMenu menu: menus) {
            if (menu.getStatus() != 0 && menu.getParentId() == parent) {
                // 如果当前菜单有父菜单
                MenuItem item = new MenuItem();
                item.setId(menu.getId());
                item.setLabel(menu.getMenuName());
                // 判断当前的菜单是否也有父菜单
                List<MenuItem> children = menuTree(menus, menu.getId());
                if (!Objects.isNull(children) && children.size() > 0) {
                    item.setChildren(children);
                }
                // 将当前菜单存放进其父菜单
                menuItems.add(item);
            }
        }
        return menuItems;
    }

    /**
     * 根据菜单类习设置默认值
     * @param menu 菜单
     */
    @Override
    public void setDefault(SysMenu menu) {
        // 如果是目录
        if (menu.getMenuType().equals("M")) {
            if (menu.getParentId() == 0) {
                menu.setComponent("Layout");
            } else {
                menu.setComponent("parentMenu");
            }
            menu.setCache(0);
            menu.setName("");
            menu.setPurview("");
        } else if (menu.getMenuType().equals("B")) {
            menu.setName("");
            menu.setPath("");
            menu.setComponent("");
            menu.setCache(0);
            menu.setAside(0);
            menu.setIcon("");
        }
    }
}
