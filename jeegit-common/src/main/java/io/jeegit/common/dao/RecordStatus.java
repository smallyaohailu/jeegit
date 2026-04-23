package io.jeegit.common.dao;

/**
 * 记录业务状态（与逻辑删除独立）。
 */
public enum RecordStatus {
    NORMAL,
    DISABLED,
    ARCHIVED
}
