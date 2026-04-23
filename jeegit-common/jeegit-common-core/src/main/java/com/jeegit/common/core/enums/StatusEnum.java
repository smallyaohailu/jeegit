package com.jeegit.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    ENABLE("0", "启用"),
    DISABLE("1", "停用");

    private final String code;
    private final String description;

    public static StatusEnum fromCode(String code) {
        for (StatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
