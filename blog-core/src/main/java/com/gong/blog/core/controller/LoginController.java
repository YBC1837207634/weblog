package com.gong.blog.core.controller;


import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.form.LoginForm;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.vo.AuthResult;
import com.gong.blog.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户登录注册")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录接口", notes = "")
    @PostMapping("/login")
    public AuthResult login(@RequestBody @Validated LoginForm loginForm) {
        String token = userService.login(loginForm);
        return AuthResult.success("登录成功！",token);
    }

    @ApiOperation(value = "注册接口", notes = "")
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Validated LoginForm loginForm) {
        if (userService.register(loginForm)) {
            return Result.success("注册成功！");
        }
        return Result.error(ResponseStatus.INTERNAL,"注册失败！");
    }

    @ApiOperation(value = "退出", notes = "")
    @GetMapping("/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success("退出成功");
    }

}
