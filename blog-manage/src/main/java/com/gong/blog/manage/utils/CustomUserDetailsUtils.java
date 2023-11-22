package com.gong.blog.manage.utils;

import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.vo.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 *  UserDetailsUtils
 */
public class CustomUserDetailsUtils {

    /**
     * 从 SecurityContext 中的 Authentication 里获取到用户信息
     * @return
     */
    public static CustomUserDetails getCustomUserDetails() {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if (Objects.nonNull(userDetails)) {
            return (CustomUserDetails) userDetails;
        }
        return new CustomUserDetails();
    }

    /**
     * 将用户信息存放到Authentication中然后将生成的Authentication存入SecurityContext
     * @param userDetails
     */
    public static void setCustomUserDetails(CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityUtils.setAuthentication(token);
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getId() {
        CustomUserDetails customUserDetails = CustomUserDetailsUtils.getCustomUserDetails();
        return customUserDetails.getSysUserDTO().getId();
    }

    /**
     * 是否是管理员
     * @return
     */
    public static boolean isAdmin() {
        CustomUserDetails userDetails = CustomUserDetailsUtils.getCustomUserDetails();
        return userDetails.isAdmin();
    }

    public static SysUserDTO getUser() {
        return CustomUserDetailsUtils.getCustomUserDetails().getSysUserDTO();
    }

}
