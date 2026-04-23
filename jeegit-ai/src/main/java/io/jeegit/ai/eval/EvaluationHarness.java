package io.jeegit.ai.eval;

import io.jeegit.ai.agent.AgentRequest;
import io.jeegit.ai.agent.AgentResponse;
import io.jeegit.ai.agent.AgentRuntime;
import io.jeegit.common.TenantContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Drives an {@link EvaluationService.EvalCase} set against a registered agent, building an {@link
 * EvalSummary} that the Open Platform exposes to release gates (see {@code EvalController}).
 *
 * <p>Each case's {@code input} field is taken as a JSON-ish key=value line list (one per line,
 * {@code key=value} per line) for simplicity; the parsed map is fed into the agent as its input
 * payload. The {@code expected} field is compared — case-insensitively — against the agent's {@code
 * reasoningSummary}.
 */
@Service
public class EvaluationHarness {

  private final AgentRuntime runtime;

  public EvaluationHarness(AgentRuntime runtime) {
    this.runtime = runtime;
  }

  public EvalSummary run(String agentId, List<EvaluationService.EvalCase> cases) {
    List<EvaluationService.EvalResult> results = new ArrayList<>();
    int passed = 0;
    long totalLatency = 0L;
    long started = System.currentTimeMillis();
    for (EvaluationService.EvalCase c : cases) {
      long caseStart = System.currentTimeMillis();
      AgentRequest request =
          new AgentRequest(agentId, null, TenantContext.tenant(), "eval", parseInput(c.input()));
      AgentResponse response;
      try {
        response = runtime.invoke(request);
      } catch (RuntimeException ex) {
        results.add(
            new EvaluationService.EvalResult(c.id(), false, 0.0, ex.getMessage(), "runtime error"));
        continue;
      }
      long took = System.currentTimeMillis() - caseStart;
      totalLatency += took;
      String actual = response.reasoningSummary() == null ? "" : response.reasoningSummary();
      boolean ok =
          c.expected() == null
              || c.expected().isBlank()
              || actual.toLowerCase().contains(c.expected().toLowerCase());
      if (ok) passed++;
      results.add(
          new EvaluationService.EvalResult(
              c.id(), ok, ok ? 1.0 : 0.0, actual, "latencyMs=" + took));
    }
    long duration = System.currentTimeMillis() - started;
    double passRate = cases.isEmpty() ? 1.0 : (double) passed / cases.size();
    return new EvalSummary(
        agentId,
        cases.size(),
        passed,
        passRate,
        cases.isEmpty() ? 0.0 : (double) totalLatency / cases.size(),
        duration,
        results);
  }

  private Map<String, Object> parseInput(String input) {
    if (input == null || input.isBlank()) return Map.of();
    java.util.Map<String, Object> out = new java.util.LinkedHashMap<>();
    for (String raw : input.split("\n")) {
      String line = raw.trim();
      int eq = line.indexOf('=');
      if (eq <= 0) continue;
      out.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
    }
    return out;
  }

  public record EvalSummary(
      String agentId,
      int total,
      int passed,
      double passRate,
      double averageLatencyMs,
      long totalDurationMs,
      List<EvaluationService.EvalResult> results) {}
}
