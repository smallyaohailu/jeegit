package io.jeegit.tech.dict;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DictTypeRepository extends JpaRepository<DictType, String> {
    Optional<DictType> findByTenantIdAndCode(String tenantId, String code);
    List<DictType> findByTenantIdOrderByCodeAsc(String tenantId);
}
