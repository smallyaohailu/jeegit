package io.jeegit.tech.iam;

import io.jeegit.common.dao.DataScope;
import io.jeegit.common.dao.TenantAwareEntity;
import io.jeegit.common.dao.TreePaths;
import io.jeegit.tech.org.Org;
import io.jeegit.tech.org.OrgRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 构造数据权限 Specification 的工厂。
 *
 * 调用约定：业务 Repository 扩展 {@code JpaSpecificationExecutor}，
 * 在服务层根据当前用户与角色拿到 {@link Specification}，与其它业务条件 {@code and()} 组合即可。
 *
 * 所有过滤动作都翻译为标准 JPA Criteria，便于在 PostgreSQL / MySQL 上一致执行。
 */
@Component
public class DataScopeSpecifications {

    private final OrgRepository orgRepository;

    public DataScopeSpecifications(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public <T extends TenantAwareEntity> Specification<T> build(
            String tenantId,
            String currentUserId,
            String currentUserOrgId,
            DataScope scope,
            String customOrgIds) {

        return (root, query, cb) -> {
            List<Predicate> all = new ArrayList<>();
            all.add(cb.equal(root.get("tenantId"), tenantId));

            if (scope == null || scope == DataScope.ALL) {
                return cb.and(all.toArray(new Predicate[0]));
            }

            switch (scope) {
                case SELF -> {
                    if (currentUserId != null) {
                        all.add(cb.equal(root.get("createdBy"), currentUserId));
                    } else {
                        all.add(cb.disjunction());
                    }
                }
                case DEPARTMENT -> {
                    if (currentUserOrgId == null) {
                        all.add(cb.disjunction());
                    } else {
                        all.add(cb.equal(root.get("orgId"), currentUserOrgId));
                    }
                }
                case DEPARTMENT_AND_CHILD -> all.add(
                        orgSubtreeIn(root, cb, tenantId, currentUserOrgId));
                case COMPANY -> all.add(
                        companyScope(root, cb, tenantId, currentUserOrgId, false));
                case COMPANY_AND_CHILD -> all.add(
                        companyScope(root, cb, tenantId, currentUserOrgId, true));
                case CUSTOM -> all.add(customScope(root, cb, customOrgIds));
                default -> { /* ALL handled above */ }
            }
            return cb.and(all.toArray(new Predicate[0]));
        };
    }

    private Predicate orgSubtreeIn(jakarta.persistence.criteria.Path<?> root,
                                    jakarta.persistence.criteria.CriteriaBuilder cb,
                                    String tenantId, String anchorOrgId) {
        if (anchorOrgId == null) {
            return cb.disjunction();
        }
        List<String> ids = collectSubtreeIds(tenantId, anchorOrgId);
        if (ids.isEmpty()) {
            return cb.disjunction();
        }
        return root.get("orgId").in(ids);
    }

    private Predicate companyScope(jakarta.persistence.criteria.Path<?> root,
                                    jakarta.persistence.criteria.CriteriaBuilder cb,
                                    String tenantId, String currentUserOrgId, boolean includeChildren) {
        Optional<Org> company = findAncestorCompany(tenantId, currentUserOrgId);
        if (company.isEmpty()) {
            return cb.disjunction();
        }
        if (!includeChildren) {
            return cb.equal(root.get("orgId"), company.get().getId());
        }
        List<String> ids = collectSubtreeIds(tenantId, company.get().getId());
        return ids.isEmpty() ? cb.disjunction() : root.get("orgId").in(ids);
    }

    private Predicate customScope(jakarta.persistence.criteria.Path<?> root,
                                   jakarta.persistence.criteria.CriteriaBuilder cb,
                                   String customOrgIds) {
        if (customOrgIds == null || customOrgIds.isBlank()) {
            return cb.disjunction();
        }
        List<String> ids = Arrays.stream(customOrgIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        return ids.isEmpty() ? cb.disjunction() : root.get("orgId").in(ids);
    }

    private List<String> collectSubtreeIds(String tenantId, String anchorOrgId) {
        List<String> result = new ArrayList<>();
        result.add(anchorOrgId);
        orgRepository.findDescendants(tenantId, TreePaths.descendantLikePattern(anchorOrgId))
                .forEach(o -> result.add(o.getId()));
        return result;
    }

    private Optional<Org> findAncestorCompany(String tenantId, String orgId) {
        if (orgId == null) return Optional.empty();
        Optional<Org> current = orgRepository.findById(orgId);
        while (current.isPresent()) {
            Org o = current.get();
            if (!tenantId.equals(o.getTenantId())) return Optional.empty();
            if (o.getType() == Org.Type.COMPANY) return Optional.of(o);
            if (o.getParentId() == null) return Optional.empty();
            current = orgRepository.findById(o.getParentId());
        }
        return Optional.empty();
    }
}
