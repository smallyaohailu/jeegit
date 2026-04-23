package io.jeegit.ai.eval;

import io.jeegit.common.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

/**
 * Evaluation endpoint. Release gates can {@code POST /api/v1/ai/eval/{agentId}/run} with a JSON
 * body that looks like:
 *
 * <pre>{@code
 * {
 *   "cases": [
 *     { "id": "c1", "input": "matterId=abc\ntitle=Tax filing", "expected": "Tax Bureau" }
 *   ]
 * }
 * }</pre>
 *
 * <p>The response contains pass count, pass rate and per-case verdicts.
 */
@RestController
@RequestMapping("/api/v1/ai/eval")
public class EvalController {

  private final EvaluationHarness harness;

  public EvalController(EvaluationHarness harness) {
    this.harness = harness;
  }

  @PostMapping("/{agentId}/run")
  public ApiResponse<EvaluationHarness.EvalSummary> run(
      @PathVariable String agentId, @RequestBody Map<String, Object> body) {
    Object rawCases = body.get("cases");
    List<EvaluationService.EvalCase> cases = new ArrayList<>();
    if (rawCases instanceof List<?> list) {
      int i = 0;
      for (Object o : list) {
        if (o instanceof Map<?, ?> rawMap) {
          @SuppressWarnings("unchecked")
          Map<String, Object> m = (Map<String, Object>) rawMap;
          String id = String.valueOf(m.getOrDefault("id", "case-" + i));
          String input = String.valueOf(m.getOrDefault("input", ""));
          String expected = String.valueOf(m.getOrDefault("expected", ""));
          cases.add(new EvaluationService.EvalCase(id, input, expected, Map.of()));
        }
        i++;
      }
    }
    if (cases.isEmpty()) {
      throw new IllegalArgumentException("'cases' array must not be empty");
    }
    return ApiResponse.ok(harness.run(agentId, cases));
  }
}
