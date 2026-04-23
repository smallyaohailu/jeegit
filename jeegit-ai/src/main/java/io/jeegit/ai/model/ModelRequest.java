package io.jeegit.ai.model;

import java.util.Map;

/**
 * A single "question to a model". The Model Gateway routes the request to the
 * concrete provider implementation based on the {@code modelKey} (and on the
 * tenant-specific provider configuration in production).
 */
public record ModelRequest(
        String tenantId,
        String modelKey,
        String prompt,
        Map<String, Object> parameters
) {
    public static ModelRequest of(String tenantId, String modelKey, String prompt) {
        return new ModelRequest(tenantId, modelKey, prompt, Map.of());
    }
}
