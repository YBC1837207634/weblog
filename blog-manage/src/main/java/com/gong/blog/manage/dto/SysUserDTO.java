package com.gong.blog.manage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysUser;

import java.util.List;


public class SysUserDTO extends SysUser {

    private List<SysRole> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> roleIds;

    private boolean admin;

    private List<String> purview;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public boolean isAdmin() {
        return super.isAdmin();
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<String> getPurview() {
        return purview;
    }

    public void setPurview(List<String> purview) {
        this.purview = purview;
    }

    @Override
    public String toString() {
        return "SysUserDTO{" +
                "roles=" + roles +
                ", roleIds=" + roleIds +
                ", admin=" + admin +
                ", purview=" + purview +
                '}';
    }
}
