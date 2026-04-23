package io.jeegit.business.matter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatterRepository extends JpaRepository<Matter, String> {
    List<Matter> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
