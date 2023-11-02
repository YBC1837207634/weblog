package com.gong.weblog.utils;

import com.gong.weblog.entity.Role;
import com.gong.weblog.vo.UserVo;

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

    public static Long getId() { return getUser().getId();}

    public static boolean isAdmin() { return getUser().getRole().getIdent().equals("admin");}

    public static Role getRole() { return getUser().getRole();}

    public static boolean isAnonymous() {
        return getUser().getId() == null;
    }
}
