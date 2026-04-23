package com.jeegit.framework.security.utils;

import cn.dev33.satoken.stp.StpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginHelper {

    public static void login(Long userId) {
        StpUtil.login(userId);
    }

    public static void login(Long userId, String device) {
        StpUtil.login(userId, device);
    }

    public static Long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    public static void logout() {
        StpUtil.logout();
    }

    public static String getToken() {
        return StpUtil.getTokenValue();
    }

    public static void checkRole(String role) {
        StpUtil.checkRole(role);
    }

    public static void checkPermission(String permission) {
        StpUtil.checkPermission(permission);
    }
}
