package io.jeegit.ai.hitl;

/**
 * 人机协同（Human-in-the-Loop）策略。
 * 与 Agent 定义中的 hitlPolicy 字段对齐。
 */
public enum HitlPolicy {
    /** 不需要人工确认。 */
    NONE,
    /** 仅 HIGH 风险动作需要人工确认。 */
    ON_HIGH_RISK,
    /** 任何动作都必须人工确认。 */
    ALWAYS
}
