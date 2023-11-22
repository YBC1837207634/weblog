package com.gong.blog.manage.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * Security 工具类
 */
public class SecurityUtils {

    /**
     * 获取用户信息
     * @return
     */
    public static UserDetails getUserDetails() {
        if (Objects.nonNull(SecurityUtils.getAuthentication())
                && !(SecurityUtils.getAuthentication() instanceof AnonymousAuthenticationToken))
            return (UserDetails) getAuthentication().getPrincipal();
        return null;
    }

    /**
     * 设置 Authentication 认证
     * @param authentication
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取认证信息
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
