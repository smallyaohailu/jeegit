package io.jeegit.common.dao;

/** Business-visible record status, independent of the logical-delete flag. */
public enum RecordStatus {
  NORMAL,
  DISABLED,
  ARCHIVED
}
