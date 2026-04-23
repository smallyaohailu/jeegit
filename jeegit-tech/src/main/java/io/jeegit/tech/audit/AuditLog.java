package io.jeegit.tech.audit;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * 审计日志 —— 仅追加，不可修改，不可删除（JPA 层禁止 update/delete）。
 * AI_GOVERNANCE.md §4 的落地载体。
 */
@Entity
@Table(name = "jeegit_audit_log",
        indexes = {
                @Index(name = "idx_audit_tenant", columnList = "tenantId"),
                @Index(name = "idx_audit_trace", columnList = "traceId"),
                @Index(name = "idx_audit_agent", columnList = "agentId")
        })
public class AuditLog {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 64)
    private String tenantId;

    @Column(length = 64)
    private String traceId;

    @Column(length = 64)
    private String agentId;

    @Column(length = 128)
    private String actorId;

    @Column(nullable = false, length = 64)
    private String action;

    @Column(length = 16)
    private String riskLevel;

    @Column(length = 32)
    private String decision;

    @Column(length = 4000)
    private String reasoningSummary;

    @Column(length = 2000)
    private String inputDigest;

    @Column(length = 2000)
    private String outputDigest;

    private long latencyMs;
    private long tokenIn;
    private long tokenOut;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt = Instant.now();

    public AuditLog() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getActorId() { return actorId; }
    public void setActorId(String actorId) { this.actorId = actorId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getReasoningSummary() { return reasoningSummary; }
    public void setReasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; }
    public String getInputDigest() { return inputDigest; }
    public void setInputDigest(String inputDigest) { this.inputDigest = inputDigest; }
    public String getOutputDigest() { return outputDigest; }
    public void setOutputDigest(String outputDigest) { this.outputDigest = outputDigest; }
    public long getLatencyMs() { return latencyMs; }
    public void setLatencyMs(long latencyMs) { this.latencyMs = latencyMs; }
    public long getTokenIn() { return tokenIn; }
    public void setTokenIn(long tokenIn) { this.tokenIn = tokenIn; }
    public long getTokenOut() { return tokenOut; }
    public void setTokenOut(long tokenOut) { this.tokenOut = tokenOut; }
    public Instant getOccurredAt() { return occurredAt; }
}
