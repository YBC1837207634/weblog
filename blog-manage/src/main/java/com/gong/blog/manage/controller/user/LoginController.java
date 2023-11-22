package com.gong.blog.manage.controller.user;

import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.common.ResponseStatus;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.vo.AuthResult;
import com.gong.blog.common.vo.Result;
import com.gong.blog.manage.dto.LoginForm;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.service.LoginService;
import com.gong.blog.manage.service.SysMenuService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import com.gong.blog.manage.vo.Route;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    private LoginService loginService;

    private SysMenuService sysMenuService;

    public LoginController(LoginService loginService, SysMenuService sysMenuService) {
        this.loginService = loginService;
        this.sysMenuService = sysMenuService;
    }

    /**
     * /login 登录接口
     * @param from
     * @return
     */
    @PostMapping("/login")
    public AuthResult login(@RequestBody LoginForm from) {
        String token = loginService.login(from.getUsername(), from.getPassword());
        if (token != null) {
            return AuthResult.success("登陆成功！",token);
        }
        return AuthResult.error(ResponseStatus.INTERNAL, "用户名或密码错误");
    }

    /**
     * /register 注册
     */
    @Log(title = "注册",businessType = BusinessType.INSERT)
    @PostMapping("/register")
    public Result<String> register(@RequestBody LoginForm from) {
        if (loginService.register(from)) {
            return Result.success("注册成功！");
        }
        return Result.error(ResponseStatus.WARN,"注册失败");
    }

    /**
     * /getInfo 返回当前登录的用户信息
     * @return
     */
    @GetMapping("/getInfo")
    public Result<SysUserDTO> getInfo() {
        return Result.success(CustomUserDetailsUtils.getCustomUserDetails().getSysUserDTO());
    }

    /**
     * 获取当前用户的路由
     */
    @GetMapping("/getRouter")
    public Result<List<Route>> getRouter() {
        if (CustomUserDetailsUtils.isAdmin()) {
            return Result.success(sysMenuService.getRouter(null));
        }
        Long id = CustomUserDetailsUtils.getId();
        return Result.success(sysMenuService.getRouter(id));
    }


}
