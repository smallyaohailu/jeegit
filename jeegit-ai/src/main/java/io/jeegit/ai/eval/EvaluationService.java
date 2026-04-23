package io.jeegit.ai.eval;

import java.util.List;
import java.util.Map;

/**
 * 评测即门禁（Eval as Gate）接入点。
 * AI_GOVERNANCE.md §7：每个 Agent/Prompt/知识库必须绑定评测集才能发布。
 *
 * MVP 仅定义契约，后续接入真实评测框架（内部 CI 或独立评测服务）。
 */
public interface EvaluationService {

    record EvalCase(String id, String input, String expected, Map<String, Object> meta) {
    }

    record EvalResult(String caseId, boolean passed, double score, String actual, String notes) {
    }

    List<EvalResult> run(String agentId, List<EvalCase> cases);
}
