package com.gong.blog.manage.vo;

import com.gong.blog.manage.dto.SysUserDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 *
 * Security 密码校验使用
 */
@Data
public class CustomUserDetails implements UserDetails {


    // 用户信息
    private SysUserDTO sysUserDTO;

    // 权限列表
//    private List<GrantedAuthority> authorities;
    private List<String> authorityList;

    public CustomUserDetails(SysUserDTO sysUserDTO, List<String> authorities) {
        this.sysUserDTO = sysUserDTO;
        this.authorityList = authorities;
    }

    public CustomUserDetails() {}

    public boolean isAdmin() {return sysUserDTO.isAdmin();}

    // 使用自定义的权限验证，不使用 GrantedAuthority
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return sysUserDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUserDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sysUserDTO.getStatus() != 0;
    }
}
