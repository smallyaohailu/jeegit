package io.jeegit.ai.eval;

import java.util.List;
import java.util.Map;

/**
 * "Eval as gate" contract. AI_GOVERNANCE.md §7 requires every agent, prompt, and knowledge-base
 * change to ship with an evaluation suite that runs in CI before the change may be released.
 *
 * <p>The preview exposes the contract only; the production harness is released as a separate
 * deliverable.
 */
public interface EvaluationService {

  record EvalCase(String id, String input, String expected, Map<String, Object> meta) {}

  record EvalResult(String caseId, boolean passed, double score, String actual, String notes) {}

  List<EvalResult> run(String agentId, List<EvalCase> cases);
}
