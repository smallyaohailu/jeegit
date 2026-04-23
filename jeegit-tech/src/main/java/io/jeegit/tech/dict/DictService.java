package io.jeegit.tech.dict;

import io.jeegit.common.TenantContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dictionary service. Provides CRUD for types / items and the "list active items for a type"
 * convenience method used by agents.
 *
 * <p>Operators can update rules through the REST API without redeploy, which extends the "Prompt /
 * rule as asset" principle of AI_GOVERNANCE.md §6 to business rules.
 */
@Service
public class DictService {

  private final DictTypeRepository typeRepo;
  private final DictItemRepository itemRepo;

  public DictService(DictTypeRepository typeRepo, DictItemRepository itemRepo) {
    this.typeRepo = typeRepo;
    this.itemRepo = itemRepo;
  }

  @Transactional
  public DictType upsertType(DictType type) {
    type.setTenantId(TenantContext.tenant());
    typeRepo
        .findByTenantIdAndCode(type.getTenantId(), type.getCode())
        .ifPresent(existing -> type.setId(existing.getId()));
    return typeRepo.save(type);
  }

  @Transactional
  public DictItem upsertItem(DictItem item) {
    item.setTenantId(TenantContext.tenant());
    itemRepo
        .findByTenantIdAndTypeCodeAndItemKey(
            item.getTenantId(), item.getTypeCode(), item.getItemKey())
        .ifPresent(existing -> item.setId(existing.getId()));
    return itemRepo.save(item);
  }

  @Transactional(readOnly = true)
  public List<DictType> listTypes() {
    return typeRepo.findByTenantIdOrderByCodeAsc(TenantContext.tenant());
  }

  @Transactional(readOnly = true)
  public List<DictItem> listItems(String typeCode) {
    return itemRepo.findByTenantIdAndTypeCodeOrderBySortOrderAscItemKeyAsc(
        TenantContext.tenant(), typeCode);
  }

  @Transactional(readOnly = true)
  public Optional<DictItem> findItem(String typeCode, String itemKey) {
    return itemRepo.findByTenantIdAndTypeCodeAndItemKey(TenantContext.tenant(), typeCode, itemKey);
  }
}
