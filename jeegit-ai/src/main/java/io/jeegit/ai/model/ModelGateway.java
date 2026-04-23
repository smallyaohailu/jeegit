package io.jeegit.ai.model;

/**
 * 模型网关契约：平台对外唯一的模型调用入口。
 * 架构宪章第 1 条 + AI_GOVERNANCE.md §5：
 *   "禁止业务代码直接调用任何厂商 SDK，所有调用必须经过本网关"。
 *
 * MVP 提供一个内置 Echo 实现用于端到端闭环验证；
 * 生产部署需注入实际厂商实现（OpenAI 兼容 / 本地 SLM 等）。
 */
public interface ModelGateway {

    ModelResponse invoke(ModelRequest request);
}
