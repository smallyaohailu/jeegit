package com.jeegit.common.core.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String module;
    private final String code;
    private final Object[] args;

    protected BaseException(String module, String code, String message, Object[] args) {
        super(message);
        this.module = module;
        this.code = code;
        this.args = args;
    }

    protected BaseException(String message) {
        this(null, null, message, null);
    }

    protected BaseException(String code, String message) {
        this(null, code, message, null);
    }
}
