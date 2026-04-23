package io.jeegit.tech.tenant;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
  Optional<Tenant> findByCode(String code);
}
