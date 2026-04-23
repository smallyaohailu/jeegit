package io.jeegit.ai.model;

import java.time.Instant;

/**
 * 模型调用响应。包含可解释所需的最小字段。
 */
public record ModelResponse(
        String modelKey,
        String content,
        long tokenIn,
        long tokenOut,
        long latencyMs,
        Instant finishedAt
) {
}
