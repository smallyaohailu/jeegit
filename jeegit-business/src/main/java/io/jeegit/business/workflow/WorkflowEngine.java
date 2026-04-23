package io.jeegit.business.workflow;

/**
 * 工作流引擎接入点（占位契约）。
 * MVP 不内置 BPMN 引擎；对接 Flowable/Camunda 时替换本实现。
 */
public interface WorkflowEngine {

    record StartOptions(String processKey, String businessKey, String tenantId) {
    }

    record Instance(String id, String processKey, String status) {
    }

    Instance start(StartOptions options);
}
