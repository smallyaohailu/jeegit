package io.jeegit.ai;

import io.jeegit.ai.eval.EvaluationService;
import io.jeegit.ai.model.EchoModelGateway;
import io.jeegit.ai.model.ModelGateway;
import io.jeegit.ai.rag.KnowledgeService;
import io.jeegit.tech.audit.AuditService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default auto-configuration for the AI core. Every bean is registered only when the consuming
 * application has not provided its own; production deployments typically override {@link
 * ModelGateway}, {@link KnowledgeService}, and {@link EvaluationService} with provider-backed
 * implementations.
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
    return (agentId, cases) ->
        cases.stream()
            .map(
                c ->
                    new EvaluationService.EvalResult(
                        c.id(), true, 1.0, c.expected(), "noop evaluator: no real model invoked"))
            .toList();
  }
}
