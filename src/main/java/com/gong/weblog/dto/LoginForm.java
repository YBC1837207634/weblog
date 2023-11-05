package com.gong.weblog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class LoginForm {

    @NotBlank(message = "用户名不为空")
    @Size(max = 64, message = "不可大于64个字符")
    private String username;

    @NotBlank(message = "密码不可为空")
    @Size(min = 5, max = 64, message = "8-64")
    private String password;

    @Size(min = 6, max = 6, message = "6个字符")
    private String code;

}
