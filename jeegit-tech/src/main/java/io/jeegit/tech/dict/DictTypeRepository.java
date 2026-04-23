package io.jeegit.tech.dict;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictTypeRepository extends JpaRepository<DictType, String> {
  Optional<DictType> findByTenantIdAndCode(String tenantId, String code);

  List<DictType> findByTenantIdOrderByCodeAsc(String tenantId);
}
