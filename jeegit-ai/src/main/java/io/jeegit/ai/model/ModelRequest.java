package io.jeegit.ai.model;

import java.util.Map;

/**
 * 模型调用请求。语义上等同于"面向模型的一次提问"。
 * ModelGateway 负责解释本对象并路由到具体厂商实现。
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
