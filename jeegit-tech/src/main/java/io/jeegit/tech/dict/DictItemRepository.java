package io.jeegit.tech.dict;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DictItemRepository extends JpaRepository<DictItem, String> {
    List<DictItem> findByTenantIdAndTypeCodeOrderBySortOrderAscItemKeyAsc(String tenantId, String typeCode);
    Optional<DictItem> findByTenantIdAndTypeCodeAndItemKey(String tenantId, String typeCode, String itemKey);
}
