package io.jeegit.business;

import io.jeegit.business.workflow.WorkflowEngine;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default wiring for the business mid-platform. Provides a {@link WorkflowEngine} stub; production
 * deployments register a real BPMN engine bean to take over.
 */
@Configuration
public class BusinessAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(WorkflowEngine.class)
  public WorkflowEngine stubWorkflowEngine() {
    return options ->
        new WorkflowEngine.Instance(UUID.randomUUID().toString(), options.processKey(), "STARTED");
  }
}
