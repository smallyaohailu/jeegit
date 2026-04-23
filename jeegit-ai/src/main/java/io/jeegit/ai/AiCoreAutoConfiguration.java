package io.jeegit.ai;

import io.jeegit.ai.eval.EvaluationService;
import io.jeegit.ai.model.EchoModelGateway;
import io.jeegit.ai.model.ModelGateway;
import io.jeegit.ai.rag.KnowledgeService;
import io.jeegit.tech.audit.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * AI 底座默认装配。仅当使用方未自行提供实现时生效。
 * 生产部署可通过注入自定义 Bean 覆盖（例如接入 OpenAI 兼容网关）。
 */
@Configuration
public class AiCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ModelGateway.class)
    public ModelGateway defaultModelGateway(AuditService auditService) {
        return new EchoModelGateway(auditService);
    }

    @Bean
    @ConditionalOnMissingBean(KnowledgeService.class)
    public KnowledgeService defaultKnowledgeService() {
        return (tenantId, query, topK) -> List.of();
    }

    @Bean
    @ConditionalOnMissingBean(EvaluationService.class)
    public EvaluationService defaultEvaluationService() {
        return (agentId, cases) -> cases.stream()
                .map(c -> new EvaluationService.EvalResult(
                        c.id(), true, 1.0, c.expected(),
                        "noop evaluator: no real model invoked"))
                .toList();
    }
}
