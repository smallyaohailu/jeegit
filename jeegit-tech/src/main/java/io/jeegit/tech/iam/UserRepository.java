package io.jeegit.tech.iam;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  List<User> findByTenantId(String tenantId);

  Optional<User> findByTenantIdAndUsername(String tenantId, String username);
}
