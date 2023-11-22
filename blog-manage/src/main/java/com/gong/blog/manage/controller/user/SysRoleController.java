package com.gong.blog.manage.controller.user;

import com.github.pagehelper.PageInfo;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.dto.RoleDTO;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.service.SysRoleService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import com.gong.blog.manage.vo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 修改角色信息，包括角色的菜单
     * @param roleDTO
     * @return
     */
    @PutMapping
    @Log(title = "修改角色", businessType = BusinessType.EDIT)
    @PreAuthorize("@s.hasAuthority('system:role:edit')")
    Result<String> update(@RequestBody RoleDTO roleDTO) {
        roleDTO.setUpdateBy(CustomUserDetailsUtils.getId());
        sysRoleService.updateRoleAndMenu(roleDTO);
        return Result.success("修改成功");
    }

    /**
     * 角色列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("@s.hasAuthority('system:role:list')")
    Result<Pages<SysRole>> list(SysRole role) {
        List<SysRole> list = sysRoleService.getList(role);
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return Result.success(new Pages<>(pageInfo.getTotal(), list.size(), pageInfo.getPageNum(), pageInfo.getPageSize(), list));
    }

    /**
     * 根据id返回对应角色
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@s.hasAuthority('system:role:list')")
    Result<SysRole> getOne(@PathVariable("id") long id) {
        return Result.success(sysRoleService.getById(id));
    }

    /**
     * /system/role
     * 添加一个角色
     * @param role
     * @return
     */
    @PostMapping
    @Log(title = "添加角色", businessType = BusinessType.INSERT)
    @PreAuthorize("@s.hasAuthority('system:role:add')")
    Result<String> save(@RequestBody RoleDTO role) {
        sysRoleService.saveRoleAndMenu(role);
        return Result.success("添加成功！");
    }

    /**
     * /system/role/5
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @Log(title = "删除角色", businessType = BusinessType.DELETE)
    @PreAuthorize("@s.hasAuthority('system:role:del')")
    Result<String> remove(@PathVariable("ids") List<Long> ids) {
        if (sysRoleService.removeByIds(ids) != 0) {
            return Result.success("删除成功");
        }
        throw new CUDException("删除失败，检查条件是否正确");
    }

}
