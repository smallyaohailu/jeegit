package io.jeegit.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jeegit.common.TenantContext;
import io.jeegit.tech.audit.AuditLog;
import io.jeegit.tech.audit.AuditService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenAI-compatible ("Chat Completions") model gateway.
 *
 * <p>Works with any vendor that speaks the OpenAI {@code /v1/chat/completions} contract — OpenAI
 * itself, Azure OpenAI, DeepSeek, Moonshot/Kimi, Qwen-API, Together.ai, local servers such as
 * Ollama's OpenAI-compatible endpoint or vLLM, etc. The tenant id is forwarded as an extra HTTP
 * header so upstream proxies can apply per-tenant quotas.
 *
 * <p>Every call produces an {@code AuditLog} entry with latency, prompt / completion tokens when
 * reported by the server, and a truncated digest of the prompt and reply.
 */
public class OpenAICompatibleModelGateway implements ModelGateway {

  private static final Logger log = LoggerFactory.getLogger(OpenAICompatibleModelGateway.class);

  private final HttpClient http;
  private final AuditService auditService;
  private final String baseUrl;
  private final String apiKey;
  private final String defaultModel;
  private final Duration timeout;
  private final ObjectMapper mapper = new ObjectMapper();

  public OpenAICompatibleModelGateway(
      AuditService auditService,
      String baseUrl,
      String apiKey,
      String defaultModel,
      Duration timeout) {
    this.auditService = auditService;
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.apiKey = apiKey;
    this.defaultModel = defaultModel;
    this.timeout = timeout == null ? Duration.ofSeconds(30) : timeout;
    this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
  }

  @Override
  public ModelResponse invoke(ModelRequest request) {
    long start = System.currentTimeMillis();
    String model =
        request.modelKey() == null || request.modelKey().isBlank()
            ? defaultModel
            : request.modelKey();

    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("model", model);
    payload.put(
        "messages",
        List.of(
            Map.of(
                "role", "system", "content", "You are a neutral, auditable enterprise assistant."),
            Map.of("role", "user", "content", request.prompt())));
    if (request.parameters() != null) {
      payload.putAll(request.parameters());
    }

    URI uri = URI.create(baseUrl + "/v1/chat/completions");
    HttpRequest.Builder builder =
        HttpRequest.newBuilder(uri)
            .timeout(timeout)
            .header("Content-Type", "application/json")
            .header(
                "X-Jeegit-Tenant",
                request.tenantId() == null ? TenantContext.tenant() : request.tenantId());
    if (apiKey != null && !apiKey.isBlank()) {
      builder.header("Authorization", "Bearer " + apiKey);
    }

    HttpRequest httpRequest;
    try {
      httpRequest =
          builder
              .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
              .build();
    } catch (Exception ex) {
      throw new IllegalStateException("unable to serialise OpenAI request body", ex);
    }

    HttpResponse<String> response;
    try {
      response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    } catch (Exception ex) {
      long latency = System.currentTimeMillis() - start;
      writeAudit(request, model, "", "DENIED", ex.getMessage(), latency, 0, 0);
      throw new IllegalStateException(
          "OpenAI-compatible endpoint call failed: " + ex.getMessage(), ex);
    }

    long latency = System.currentTimeMillis() - start;
    if (response.statusCode() / 100 != 2) {
      writeAudit(
          request,
          model,
          "",
          "DENIED",
          "HTTP " + response.statusCode() + " from " + uri,
          latency,
          0,
          0);
      throw new IllegalStateException(
          "OpenAI-compatible gateway returned HTTP " + response.statusCode());
    }

    OpenAIResponse parsed;
    try {
      parsed = mapper.readValue(response.body(), OpenAIResponse.class);
    } catch (Exception ex) {
      writeAudit(
          request,
          model,
          "",
          "DENIED",
          "unable to parse response: " + ex.getMessage(),
          latency,
          0,
          0);
      throw new IllegalStateException("unable to parse OpenAI response", ex);
    }

    String content = parsed.firstContent();
    long tokenIn = parsed.usage == null ? 0 : parsed.usage.promptTokens;
    long tokenOut = parsed.usage == null ? 0 : parsed.usage.completionTokens;
    writeAudit(
        request, model, content, "ALLOW", "OpenAI-compatible gateway", latency, tokenIn, tokenOut);

    return new ModelResponse(model, content, tokenIn, tokenOut, latency, Instant.now());
  }

  private void writeAudit(
      ModelRequest request,
      String model,
      String content,
      String decision,
      String reason,
      long latency,
      long tokenIn,
      long tokenOut) {
    try {
      AuditLog entry = new AuditLog();
      entry.setTenantId(request.tenantId());
      entry.setAction("MODEL_CALL");
      entry.setRiskLevel("LOW");
      entry.setDecision(decision);
      entry.setReasoningSummary(reason + " [model=" + model + "]");
      entry.setInputDigest(abbreviate(request.prompt()));
      entry.setOutputDigest(abbreviate(content));
      entry.setLatencyMs(latency);
      entry.setTokenIn(tokenIn);
      entry.setTokenOut(tokenOut);
      auditService.record(entry);
    } catch (RuntimeException ex) {
      log.warn("failed to persist audit entry for model call", ex);
    }
  }

  private static String abbreviate(String s) {
    if (s == null) return null;
    return s.length() > 512 ? s.substring(0, 512) + "..." : s;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class OpenAIResponse {
    public List<Choice> choices;
    public Usage usage;

    String firstContent() {
      if (choices == null || choices.isEmpty()) return "";
      Choice first = choices.get(0);
      return first == null || first.message == null ? "" : first.message.content;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Choice {
    public Message message;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Message {
    public String role;
    public String content;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Usage {
    @com.fasterxml.jackson.annotation.JsonProperty("prompt_tokens")
    public long promptTokens;

    @com.fasterxml.jackson.annotation.JsonProperty("completion_tokens")
    public long completionTokens;

    @com.fasterxml.jackson.annotation.JsonProperty("total_tokens")
    public long totalTokens;
  }
}
