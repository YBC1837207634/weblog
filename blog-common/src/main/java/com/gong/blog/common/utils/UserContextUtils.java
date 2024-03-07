package com.gong.blog.common.utils;

import com.gong.blog.common.entity.Role;
import com.gong.blog.common.vo.UserVo;

public class UserContextUtils {

    private static final ThreadLocal<UserVo> userLocal = new ThreadLocal<>();

    public static void setUser(UserVo details) {
        userLocal.set(details);
    }

    public static UserVo getUser() {
        return userLocal.get();
    }

    public static void remove() {
        userLocal.remove();
    }

    public static Long getId() {
        UserVo user = getUser();
        if (user == null)
            return 1L;
        return user.getId();
    }

    public static boolean isAdmin() { return getUser().getRole().getIdent().equals("admin");}

    public static Role getRole() { return getUser().getRole();}

}
