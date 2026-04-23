package io.jeegit.ai.model;

/**
 * Single choke point for every LLM call made inside the platform.
 *
 * <p>Per AI_GOVERNANCE.md §5, business code is not allowed to invoke any vendor
 * SDK directly; the gateway owns authentication, quota, cost accounting,
 * PII redaction, and audit. The preview ships an {@code EchoModelGateway}
 * for self-contained demos; production deployments inject a real
 * implementation (OpenAI-compatible, local SLM, etc.).</p>
 */
public interface ModelGateway {

    ModelResponse invoke(ModelRequest request);
}
