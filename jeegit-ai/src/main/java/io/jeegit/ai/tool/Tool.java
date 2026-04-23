package io.jeegit.ai.tool;

import java.util.Map;

/**
 * 工具（Tool）—— Agent 调用外部能力的统一抽象。
 * 遵循 AI_GOVERNANCE.md §2：工具以白名单方式显式授予 Agent 使用权。
 */
public interface Tool {

    /**
     * 全局唯一工具名。建议 domain.action 形式，例如 matter.dispatch。
     */
    String name();

    /**
     * 一句话说明（将写入审计与开放平台 API 资产目录）。
     */
    String description();

    /**
     * 风险等级：LOW / MEDIUM / HIGH。HIGH 默认触发 HITL 阻塞。
     */
    String riskLevel();

    /**
     * 执行工具。params 中必含 tenantId。
     */
    Map<String, Object> execute(Map<String, Object> params);
}
