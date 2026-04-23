package io.jeegit.tech.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrgRepository extends JpaRepository<Org, String> {

    Optional<Org> findByTenantIdAndCode(String tenantId, String code);

    List<Org> findByTenantIdOrderByTreeLevelAscTreeSortAsc(String tenantId);

    List<Org> findByTenantIdAndParentIdOrderByTreeSortAsc(String tenantId, String parentId);

    @Query("SELECT o FROM Org o WHERE o.tenantId = :tenantId AND o.parentIds LIKE :pattern ORDER BY o.treeLevel ASC, o.treeSort ASC")
    List<Org> findDescendants(String tenantId, String pattern);
}
