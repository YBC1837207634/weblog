package com.gong.blog.manage.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * user 表
 */

public class SysUser extends BaseEntity {

    @ExcelIgnore
    private Long id;

    @NotBlank(message = "用户名不可为空")
    @Size(max = 30, min = 1, message = "长度在1-30")
    private String username;

    @ExcelIgnore
    @NotBlank(message = "密码不可为空")
    @Size(max = 20, min = 1, message = "长度在1-20")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 30, message = "昵称不可以超过30个字符")
    private String nickname;

    @Pattern(regexp = "[男女]", message = "性别格式错误")
    private String gender;

    private String avatar;

    @Email(message = "邮箱格式错误")
    private String mail;

    @Size(min = 11, max=11, message = "手机号格式不正确")
    private String phone;

    private String userType;

    private String signature;

    private Integer status;

    private Long createBy;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long id) {
        return id != null && id == 1L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (Objects.nonNull(nickname)) {
            this.nickname = nickname.trim();
        } else {
            this.nickname = null;
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (Objects.nonNull(avatar)) {
            this.avatar = avatar.trim();
        } else {
            this.avatar = null;
        }
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (Objects.nonNull(mail)) {
            this.mail = mail.trim();
        } else {
            this.mail = null;
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (Objects.nonNull(phone)) {
            this.phone = phone.trim();
        } else {
            this.phone = null;
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        if (Objects.nonNull(signature)) {
            this.signature = signature.trim();
        } else {
            this.signature = null;
        }
    }

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", userType='" + userType + '\'' +
                ", signature='" + signature + '\'' +
                ", status=" + status +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

