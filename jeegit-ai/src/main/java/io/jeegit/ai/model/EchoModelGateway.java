package io.jeegit.ai.model;

import io.jeegit.tech.audit.AuditLog;
import io.jeegit.tech.audit.AuditService;

import java.time.Instant;

/**
 * 默认 Echo 实现。仅用于打通治理闭环（审计、评测、HITL）。
 * 真实部署请用 Ollama / OpenAI 兼容 / 本地 SLM 实现覆盖。
 */
public class EchoModelGateway implements ModelGateway {

    private final AuditService auditService;

    public EchoModelGateway(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public ModelResponse invoke(ModelRequest request) {
        long start = System.currentTimeMillis();
        String content = "[echo:" + request.modelKey() + "] " + request.prompt();
        long latency = System.currentTimeMillis() - start;

        AuditLog log = new AuditLog();
        log.setTenantId(request.tenantId());
        log.setAction("MODEL_CALL");
        log.setRiskLevel("LOW");
        log.setDecision("ALLOW");
        log.setReasoningSummary("echo gateway; no external call performed");
        log.setInputDigest(abbreviate(request.prompt()));
        log.setOutputDigest(abbreviate(content));
        log.setLatencyMs(latency);
        log.setTokenIn(request.prompt() == null ? 0 : request.prompt().length());
        log.setTokenOut(content.length());
        auditService.record(log);

        return new ModelResponse(request.modelKey(), content,
                log.getTokenIn(), log.getTokenOut(), latency, Instant.now());
    }

    private String abbreviate(String s) {
        if (s == null) return null;
        return s.length() > 512 ? s.substring(0, 512) + "..." : s;
    }
}
