package com.jeegit.common.core.utils;

import cn.hutool.core.util.StrUtil;

public final class StringUtils extends StrUtil {

    private StringUtils() {
    }

    public static boolean isHttpUrl(String url) {
        if (isBlank(url)) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String toCamelCase(String name) {
        return StrUtil.toCamelCase(name);
    }

    public static String toUnderlineCase(String name) {
        return StrUtil.toUnderlineCase(name);
    }

    public static String format(String template, Object... params) {
        return StrUtil.format(template, params);
    }
}
