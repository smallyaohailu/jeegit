package com.jeegit.common.core.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class CommonConstants {

    private CommonConstants() {
    }

    // ==================== HTTP Status ====================

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;

    // ==================== Pagination ====================

    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    // ==================== Common Status ====================

    public static final String STATUS_NORMAL = "0";
    public static final String STATUS_DISABLED = "1";

    // ==================== Logical Delete ====================

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;

    // ==================== Roles ====================

    public static final String SUPER_ADMIN_ROLE_KEY = "admin";

    // ==================== Encoding ====================

    public static final String UTF8 = "UTF-8";
    public static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;
}
