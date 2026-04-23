package io.jeegit.common.dao;

import io.jeegit.common.TenantContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 将 Spring Data JPA 的审计"当前用户"桥接到 {@link TenantContext#actor()}.
 * 所有 {@code @CreatedBy / @LastModifiedBy} 都从此处取值。
 */
@Component
public class TenantAwareAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(TenantContext.actor());
    }
}
