package io.jeegit.tech.iam;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByTenantId(String tenantId);
    List<User> findByTenantIdAndDepartment(String tenantId, String department);
}
