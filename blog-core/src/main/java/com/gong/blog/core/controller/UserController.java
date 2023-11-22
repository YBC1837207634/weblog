package com.gong.blog.core.controller;


import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.form.LoginForm;
import com.gong.blog.common.form.UserForm;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.common.vo.Result;
import com.gong.blog.common.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = {"用户"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "获取当前登录的用户信息", notes = "")
    @GetMapping("/vo")
    public Result<UserVo> userInfo() {
        return Result.success(UserContextUtils.getUser());
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
        // 信息更新后，更新redis
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set("user:user_" + UserContextUtils.getUser().getUsername(),userService.getUserVo(user.getId()));
        return Result.success("更新成功！");
    }

    @PutMapping("/pwd")
    @ApiOperation(value = "修改密码", notes = "")
    @Log(title = "修改密码", businessType = BusinessType.EDIT, onlyError = true)
    public Result<String> updatePwd(@RequestBody @Validated LoginForm form) {
        userService.updatePwd(form);
        userService.logout();
        return Result.success("修改成功");
    }


}
