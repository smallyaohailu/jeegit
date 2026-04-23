package io.jeegit.common.dao;

/**
 * Logical-delete marker. Kept as a dedicated enum so rows that are pending
 * audit ({@link #AUDITING}) remain queryable without a third boolean column.
 */
public enum DeleteFlag {
    NORMAL,
    DELETED,
    AUDITING
}
