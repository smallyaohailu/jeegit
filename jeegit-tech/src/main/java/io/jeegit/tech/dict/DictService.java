package io.jeegit.tech.dict;

import io.jeegit.common.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 字典服务。提供：
 *  - 类型与项的 CRUD / 查询
 *  - Agent 可复用的"按类型取全部启用项"便利方法
 *
 * Agent 运行时通过本服务读取规则，运营可在不重启应用的前提下调整业务规则，
 * 契合 AI_GOVERNANCE.md §6"Prompt/规则资产化"的思想在业务规则层的延伸。
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
        typeRepo.findByTenantIdAndCode(type.getTenantId(), type.getCode())
                .ifPresent(existing -> type.setId(existing.getId()));
        return typeRepo.save(type);
    }

    @Transactional
    public DictItem upsertItem(DictItem item) {
        item.setTenantId(TenantContext.tenant());
        itemRepo.findByTenantIdAndTypeCodeAndItemKey(
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
        return itemRepo.findByTenantIdAndTypeCodeAndItemKey(
                TenantContext.tenant(), typeCode, itemKey);
    }
}
