package io.jeegit.business.matter;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MatterRepository
    extends JpaRepository<Matter, String>, JpaSpecificationExecutor<Matter> {
  List<Matter> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
