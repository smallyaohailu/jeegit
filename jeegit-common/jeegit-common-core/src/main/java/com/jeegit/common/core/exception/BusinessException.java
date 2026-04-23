package com.jeegit.common.core.exception;

import com.jeegit.common.core.result.IResultCode;

import java.io.Serial;

public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    private BusinessException(String module, String code, String message, Object[] args) {
        super(module, code, message, args);
    }

    public static BusinessException of(String message) {
        return new BusinessException(null, null, message, null);
    }

    public static BusinessException of(String code, String message) {
        return new BusinessException(null, code, message, null);
    }

    public static BusinessException of(IResultCode resultCode) {
        return new BusinessException(null, String.valueOf(resultCode.getCode()), resultCode.getMessage(), null);
    }
}
