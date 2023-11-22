package com.gong.blog.manage.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

/**
 * 基本用户数据
 */
public class BaseUserInfo {

    private Long id;

    @Size(max = 30, message = "昵称不可以超过30个字符")
    private String nickname;

    @Pattern(regexp = "[男女]", message = "性别格式错误")
    private String gender;

    private String avatar;

    @Email(message = "邮箱格式错误")
    private String mail;

    @Size(min = 11, max=11, message = "手机号格式不正确")
    private String phone;

    private Integer status;

    private String signature;
    // 角色id
    private Set<Long> roleIds;

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

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "BaseUserInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", signature='" + signature + '\'' +
                ", roleIds=" + roleIds +
                '}';
    }
}
