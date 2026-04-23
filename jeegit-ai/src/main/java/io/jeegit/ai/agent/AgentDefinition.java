package io.jeegit.ai.agent;

import io.jeegit.ai.hitl.HitlPolicy;

import java.util.Set;

/**
 * Agent 定义——以数据形式描述一个"数字员工"。
 * 对应 AI_GOVERNANCE.md §2 的权限模型。
 */
public record AgentDefinition(
        String agentId,
        String tenantId,
        String ownerId,
        String description,
        Set<String> roles,
        Set<String> allowedTools,
        String riskLevel,
        HitlPolicy hitlPolicy
) {
}
