package io.jeegit.business.matter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MatterRepository extends JpaRepository<Matter, String>, JpaSpecificationExecutor<Matter> {
    List<Matter> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
