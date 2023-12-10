package com.gong.blog.manage.controller.user;

import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.entity.SysMenu;
import com.gong.blog.manage.service.SysMenuService;
import com.gong.blog.manage.vo.MenuItem;
import com.gong.blog.manage.vo.MenuSelect;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    private SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PostMapping
    @Log(title = "添加菜单", businessType = BusinessType.INSERT)
    @PreAuthorize("@s.hasAuthority('system:menu:add')")
    Result<String> save(@RequestBody @Validated SysMenu menu) {
        if (sysMenuService.saveOne(menu) != 0) {
            return Result.success("添加成功！");
        }
        return Result.error(ResponseStatus.NOT_MODIFY, "添加失败");
    }

    @PutMapping
    @Log(title = "修改菜单", businessType = BusinessType.EDIT)
    @PreAuthorize("@s.hasAuthority('system:menu:edit')")
    Result<String> update(@RequestBody @Validated SysMenu menu) {
        if (sysMenuService.updateSysMenuById(menu) != 0) {
            return Result.success("更新成功！");
        }
        return Result.error(ResponseStatus.NOT_MODIFY, "更新失败");
    }

    /**
     * 菜单列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("@s.hasAuthority('system:menu:list')")
    Result<List<SysMenu>> list() {
        return Result.success(sysMenuService.getList(null));
    }

    /**
     * 根据用户id获取菜单树
     * @param id
     * @return
     */
    @GetMapping("/menuTreeSelect/{id}")
    @PreAuthorize("@s.hasAuthority('system:menu:list')")
    Result<MenuSelect> menuTreeSelect(@PathVariable("id") long id) {
        List<MenuItem> menuTree = sysMenuService.getMenuTree();
        List<SysMenu> menuList = sysMenuService.getMenuListByRoleId(id);
        List<Long> longs = menuList.stream().map(SysMenu::getId).toList();
        return Result.success(new MenuSelect(menuTree, longs));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/menuTree")
    Result<List<MenuItem>> menuTree() {
        List<MenuItem> menuTree = sysMenuService.getMenuTree();
        return Result.success(menuTree);
    }

    /**
     * 根据菜单id获取菜单信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@s.hasAuthority('system:menu:list')")
    Result<SysMenu> userMenuList(@PathVariable("id") long id) {
        return Result.success(sysMenuService.getById(id));
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Log(title = "删除菜单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("@s.hasAuthority('system:menu:del')")
    Result<String> remove(@PathVariable("id") long id) {
        if (sysMenuService.removeById(id) != 0) {
            return Result.success("删除成功");
        }
        throw new CUDException("删除失败，检查条件是否正确");

    }

}
