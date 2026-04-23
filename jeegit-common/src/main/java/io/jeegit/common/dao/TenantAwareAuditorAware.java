package io.jeegit.common.dao;

import io.jeegit.common.TenantContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Bridges Spring Data JPA auditing to {@link TenantContext#actor()} so every
 * {@code @CreatedBy} / {@code @LastModifiedBy} field is populated with the
 * current actor identifier without extra wiring.
 */
@Component
public class TenantAwareAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(TenantContext.actor());
    }
}
