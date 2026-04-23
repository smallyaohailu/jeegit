package io.jeegit.common.dao;

/**
 * 逻辑删除标志。与 jeesite 语义对齐但使用枚举以增强类型安全。
 */
public enum DeleteFlag {
    NORMAL,
    DELETED,
    AUDITING
}
