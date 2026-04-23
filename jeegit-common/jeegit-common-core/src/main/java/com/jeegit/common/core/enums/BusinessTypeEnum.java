package com.jeegit.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {

    OTHER(0, "其他"),
    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    EXPORT(4, "导出"),
    IMPORT(5, "导入"),
    GRANT(6, "授权"),
    FORCE_LOGOUT(7, "强退");

    private final int ordinalValue;
    private final String description;

    public static BusinessTypeEnum fromOrdinal(int ordinal) {
        for (BusinessTypeEnum type : values()) {
            if (type.ordinalValue == ordinal) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown business type ordinal: " + ordinal);
    }
}
