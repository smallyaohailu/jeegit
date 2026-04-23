package com.jeegit.common.core.exception;

import com.jeegit.common.core.result.IResultCode;

import java.io.Serial;

public class AuthException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    private AuthException(String module, String code, String message, Object[] args) {
        super(module, code, message, args);
    }

    public static AuthException of(String message) {
        return new AuthException(null, null, message, null);
    }

    public static AuthException of(String code, String message) {
        return new AuthException(null, code, message, null);
    }

    public static AuthException of(IResultCode resultCode) {
        return new AuthException(null, String.valueOf(resultCode.getCode()), resultCode.getMessage(), null);
    }
}
