package io.jeegit.ai.hitl;

/**
 * Human-in-the-loop approval policy declared on an agent definition.
 */
public enum HitlPolicy {
    /** No approval is required. */
    NONE,
    /** Only {@code HIGH}-risk actions require a human approval. */
    ON_HIGH_RISK,
    /** Every action requires human approval before execution. */
    ALWAYS
}
