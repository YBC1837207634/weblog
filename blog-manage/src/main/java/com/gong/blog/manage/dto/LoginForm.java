package com.gong.blog.manage.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 接受登陆表单
 */

public class LoginForm {
    @NotBlank(message = "用户名不可为空")
    @Size(max = 30, min = 1, message = "长度在1-30")
    private String username;

    @NotBlank(message = "密码不可为空")
    @Size(max = 20, min = 1, message = "长度在1-20")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
