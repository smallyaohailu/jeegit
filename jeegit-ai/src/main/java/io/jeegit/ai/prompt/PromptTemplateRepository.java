package io.jeegit.ai.prompt;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptTemplateRepository extends JpaRepository<PromptTemplate, String> {

  Optional<PromptTemplate> findByTenantIdAndCodeAndVersion(
      String tenantId, String code, int version);

  Optional<PromptTemplate> findFirstByTenantIdAndCodeAndPublishedTrueOrderByVersionDesc(
      String tenantId, String code);

  List<PromptTemplate> findByTenantIdAndCodeOrderByVersionDesc(String tenantId, String code);

  List<PromptTemplate> findByTenantIdOrderByCodeAscVersionDesc(String tenantId);
}
