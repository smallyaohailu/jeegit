package io.jeegit.ai.model;

import java.time.Instant;

/**
 * Structured response from a model invocation. Carries only the fields required to audit and
 * explain the call; provider-specific metadata should be stored under {@code parameters} of the
 * request if needed.
 */
public record ModelResponse(
    String modelKey,
    String content,
    long tokenIn,
    long tokenOut,
    long latencyMs,
    Instant finishedAt) {}
