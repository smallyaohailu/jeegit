package com.jeegit.common.core.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ServletUtils {

    private static final String UNKNOWN = "unknown";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private ServletUtils() {
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No current servlet request bound to thread");
        }
        return attrs.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No current servlet response bound to thread");
        }
        return attrs.getResponse();
    }

    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    public static String getClientIp() {
        HttpServletRequest request = getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        String xRequestedWith = request.getHeader(X_REQUESTED_WITH);
        if (XML_HTTP_REQUEST.equalsIgnoreCase(xRequestedWith)) {
            return true;
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/json");
    }

    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return sra;
        }
        return null;
    }

    private static boolean isValidIp(String ip) {
        return StrUtil.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }
}
