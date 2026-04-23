package io.jeegit.ai;

import io.jeegit.ai.eval.EvaluationService;
import io.jeegit.ai.model.EchoModelGateway;
import io.jeegit.ai.model.ModelGateway;
import io.jeegit.ai.model.OpenAICompatibleModelGateway;
import io.jeegit.ai.rag.KnowledgeChunkRepository;
import io.jeegit.ai.rag.KnowledgeService;
import io.jeegit.ai.rag.PgVectorKnowledgeService;
import io.jeegit.tech.audit.AuditService;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Default wiring for the AI core.
 *
 * <p>Two model-gateway paths ship:
 *
 * <ul>
 *   <li>{@link OpenAICompatibleModelGateway} — selected when {@code
 *       jeegit.ai.model.provider=openai-compat}.
 *   <li>{@link EchoModelGateway} — no-network demo fallback (and the default).
 * </ul>
 *
 * Production deployments register their own {@link ModelGateway} / {@link KnowledgeService} /
 * {@link EvaluationService} bean to override either path.
 */
@Configuration
public class AiCoreAutoConfiguration {

  @Value("${jeegit.ai.model.base-url:http://localhost:1234}")
  private String modelBaseUrl;

  @Value("${jeegit.ai.model.api-key:}")
  private String modelApiKey;

  @Value("${jeegit.ai.model.default-model:gpt-4o-mini}")
  private String defaultModel;

  @Value("${jeegit.ai.model.timeout-seconds:30}")
  private long timeoutSeconds;

  @Bean
  @ConditionalOnMissingBean(ModelGateway.class)
  @ConditionalOnProperty(name = "jeegit.ai.model.provider", havingValue = "openai-compat")
  public ModelGateway openAICompatibleModelGateway(AuditService auditService) {
    return new OpenAICompatibleModelGateway(
        auditService, modelBaseUrl, modelApiKey, defaultModel, Duration.ofSeconds(timeoutSeconds));
  }

  @Bean
  @ConditionalOnMissingBean(ModelGateway.class)
  public ModelGateway defaultModelGateway(AuditService auditService) {
    return new EchoModelGateway(auditService);
  }

  @Bean
  @Profile({"pgvector", "postgres"})
  @ConditionalOnMissingBean(KnowledgeService.class)
  public KnowledgeService pgVectorKnowledgeService(KnowledgeChunkRepository repository) {
    return new PgVectorKnowledgeService(repository);
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
