package com.gong.blog.manage.controller.user;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.github.pagehelper.PageInfo;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.dto.BaseUserInfo;
import com.gong.blog.manage.dto.LoginForm;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysUser;
import com.gong.blog.manage.service.SysUserService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import com.gong.blog.manage.utils.UploadDataListener;
import com.gong.blog.manage.vo.Pages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("system/user")
public class SysUserController {

    private SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * system/user/space
     * 更新用户个人信息
     *
     * @param from
     * @return
     */
    @Log(title = "更新个人信息", businessType = BusinessType.EDIT)
    @PutMapping("/space")
    public Result<String> updateSpace(@RequestBody BaseUserInfo from) {
        from.setId(CustomUserDetailsUtils.getId());
        sysUserService.updateBaseUserInfo(from);
        return Result.success("更新成功");
    }

    /**
     * system/user/space
     * 个人中心的数据
     *
     * @return
     */
    @GetMapping("/space")
    public Result<SysUser> getInfo() {
        SysUser sysUser = sysUserService.getById(CustomUserDetailsUtils.getId());
        return Result.success(sysUser);
    }


    /**
     * /system/user/updatePwd 修改密码
     *
     * @param from
     * @return
     */
    @Log(title = "修改密码", businessType = BusinessType.EDIT)
    @PutMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody LoginForm from) {
        SysUser user = new SysUser();
        user.setId(CustomUserDetailsUtils.getId());
        user.setPassword(from.getPassword());
        sysUserService.updatePwd(user);
        return Result.success("修改成功");
    }

    /**
     * 通过用户id
     * @return SysUserDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("@s.hasAuthority('system:user:list')")
    Result<SysUserDTO> byId(@PathVariable("id") Long id) {
        SysUser user = sysUserService.getById(id);
        List<SysRole> roles = sysUserService.getSysRoleByUserId(id);
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO, "roles");
        userDTO.setRoles(roles);
        return Result.success(userDTO);
    }

    /**
     * system/user/list
     * 用户分页查询
     * @return 分页数据
     */
    @GetMapping("/list")
    @PreAuthorize("@s.hasAuthority('system:user:list')")
    public Result<Pages<SysUser>> page(SysUser pageParams) {
        List<SysUser> list = sysUserService.getList(pageParams);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return Result.success(new Pages<>(pageInfo.getTotal(), list.size(), pageInfo.getPageNum(), pageInfo.getPageSize(), list));
    }

    /**
     * system/user
     * 根据id修改用户的信息和分配角色
     * @param
     * @return
     */
    @PutMapping
    @Log(title = "修改用户的信息", businessType = BusinessType.EDIT)
    @PreAuthorize("@s.hasAuthority('system:user:edit')")
    public Result<String> update(@RequestBody BaseUserInfo from) {
        if (from.getId() != null ) {
            sysUserService.updateUserRoleById(from);
            return Result.success("更新成功");
        }
        return Result.error(ResponseStatus.NOT_MODIFY, "更新失败!");
    }

    /**
     * system/user
     * 添加一个用户
     * @param user
     * @return
     */
    @PostMapping
    @Log(title = "添加用户", businessType = BusinessType.INSERT)
    @PreAuthorize("@s.hasAuthority('system:user:add')")
    public Result<String> save(@RequestBody SysUserDTO user) {
        if (StringUtils.hasText(user.getPassword()))
            user.setPassword("123456");
        sysUserService.saveUserAssignRole(user);
        return Result.success("添加成功！");
    }

    /**
     * system/user/1,2
     * 根据id删除用户
     * @param ids  用户id
     * @return Result<String>
     */
    @DeleteMapping("/{ids}")
    @Log(title = "删除用户", businessType = BusinessType.DELETE)
    @PreAuthorize("@s.hasAuthority('system:user:del')")
    public Result<String> remove(@PathVariable List<Long> ids) {
        if (sysUserService.removeByIds(ids) != 0) {
            return Result.success("删除成功");
        }
        throw new CUDException("删除失败，检查条件是否正确");
    }

    /**
     * 导出用户信息
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    @PreAuthorize("@s.hasAuthority('system:user:export')")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SysUser.class).sheet("模板").doWrite(sysUserService.getList(null));
    }

    /**
     * 文件上传
     * <p>1. 创建excel对应的实体对象
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
     * <p>3. 直接读即可
     */
    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("@s.hasAuthority('system:user:import')")
    public Result<String> upload(MultipartFile file)throws IOException{
        EasyExcel.read(file.getInputStream(),SysUser.class,new UploadDataListener(sysUserService)).sheet().doRead();
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        return Result.success("导入成功");
    }

}
