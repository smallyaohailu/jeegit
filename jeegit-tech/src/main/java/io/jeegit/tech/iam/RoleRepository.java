package io.jeegit.tech.iam;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
  List<Role> findByTenantIdOrderByCodeAsc(String tenantId);

  Optional<Role> findByTenantIdAndCode(String tenantId, String code);
}
