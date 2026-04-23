package io.jeegit.tech.org;

import io.jeegit.common.TenantContext;
import io.jeegit.common.dao.TreePaths;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrgService {

    private final OrgRepository repository;

    public OrgService(OrgRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Org create(Org org, String parentId) {
        org.setTenantId(TenantContext.tenant());
        if (parentId == null || parentId.isBlank()) {
            org.setParentId(null);
            org.setParentIds(TreePaths.rootPath());
            org.setTreeLevel(0);
        } else {
            Org parent = repository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("parent org not found: " + parentId));
            if (!parent.getTenantId().equals(org.getTenantId())) {
                throw new IllegalStateException("cross-tenant parent rejected");
            }
            org.setParentId(parentId);
            org.setParentIds(TreePaths.childPath(parent.getParentIds(), parent.getId()));
            org.setTreeLevel(parent.getTreeLevel() + 1);
            if (parent.isTreeLeaf()) {
                parent.setTreeLeaf(false);
                repository.save(parent);
            }
        }
        org.setTreeLeaf(true);
        return repository.save(org);
    }

    @Transactional(readOnly = true)
    public List<Org> listForCurrentTenant() {
        return repository.findByTenantIdOrderByTreeLevelAscTreeSortAsc(TenantContext.tenant());
    }

    @Transactional(readOnly = true)
    public List<Org> listChildren(String parentId) {
        return repository.findByTenantIdAndParentIdOrderByTreeSortAsc(TenantContext.tenant(), parentId);
    }

    @Transactional(readOnly = true)
    public List<Org> listDescendants(String nodeId) {
        return repository.findDescendants(TenantContext.tenant(), TreePaths.descendantLikePattern(nodeId));
    }

    @Transactional(readOnly = true)
    public Org get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("org not found: " + id));
    }
}
