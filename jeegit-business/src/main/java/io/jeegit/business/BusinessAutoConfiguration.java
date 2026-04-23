package io.jeegit.business;

import io.jeegit.business.workflow.WorkflowEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 业务中台默认装配：提供 WorkflowEngine 的占位实现，
 * 接入真实 BPMN 引擎时用自定义 Bean 覆盖。
 */
@Configuration
public class BusinessAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(WorkflowEngine.class)
    public WorkflowEngine stubWorkflowEngine() {
        return options -> new WorkflowEngine.Instance(
                UUID.randomUUID().toString(), options.processKey(), "STARTED");
    }
}
