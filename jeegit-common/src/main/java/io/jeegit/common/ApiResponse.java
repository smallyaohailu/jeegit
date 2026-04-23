package io.jeegit.common;

import java.time.Instant;
import java.util.Map;

/**
 * 统一响应体。所有开放平台 API 的返回值都应使用此结构。
 */
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        Instant timestamp,
        Map<String, Object> meta
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", "success", data, Instant.now(), Map.of());
    }

    public static <T> ApiResponse<T> ok(T data, Map<String, Object> meta) {
        return new ApiResponse<>(true, "OK", "success", data, Instant.now(), meta);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null, Instant.now(), Map.of());
    }
}
