package com.gong.weblog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserForm {

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不可为空")
    private String nickname;


    /**
     * 头像
     */
    @Size(max = 255, message = "url不可超过255个字符")
    private String avatar;


    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     * 手机号
     */
    @Size(min = 11,max = 11, message = "手机号格式错误")
    private String phone;

    private String gender;

    /**
     * 背景
     */
    @Size(max = 255, message = "url不可超过255个字符")
    private String background;

    @Size(max = 255, message = "description不可超过255个字符")
    private String description;
}
