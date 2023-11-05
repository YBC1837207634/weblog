package com.gong.weblog.controller;


import com.gong.weblog.annotation.Log;
import com.gong.weblog.dto.LoginForm;
import com.gong.weblog.dto.UserForm;
import com.gong.weblog.entity.User;
import com.gong.weblog.enums.BusinessType;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.Result;
import com.gong.weblog.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = {"用户"})
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "获取当前登录的用户信息", notes = "")
    @GetMapping("/vo")
    public Result<UserVo> userInfo() {
        return Result.success(userService.getUserVo(UserContextUtils.getId()));
    }

    @ApiOperation(value = "获取指定用户的基本信息", notes = "")
    @GetMapping("/vo/{id}")
    public Result<UserVo> getUser(@PathVariable("id") Long id) {
        return Result.success(userService.getUserVo(id));
    }

    @PutMapping
    @ApiOperation(value = "更新用户基本信息", notes = "")
    @Log(title = "更新用户信息", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> update(@RequestBody @Validated UserForm form) {
        User user = new User();
        BeanUtils.copyProperties(form, user);
        user.setId(UserContextUtils.getId());
        userService.updateById(user);
        return Result.success("更新成功！");
    }

    @PutMapping("/pwd")
    @ApiOperation(value = "修改密码", notes = "")
    @Log(title = "修改密码", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> updatePwd(@RequestBody @Validated LoginForm form) {
        userService.updatePwd(form);
        return Result.success("修改成功");
    }


}
