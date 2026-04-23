package io.jeegit.business.workflow;

/**
 * Workflow engine integration point. The preview ships a stub implementation; production
 * deployments can plug in Flowable or Camunda by providing a {@code WorkflowEngine} bean that
 * overrides the default.
 */
public interface WorkflowEngine {

  record StartOptions(String processKey, String businessKey, String tenantId) {}

  record Instance(String id, String processKey, String status) {}

  Instance start(StartOptions options);
}
