package io.jeegit.ai.prompt;

import io.jeegit.common.TenantContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** CRUD + simple templating helpers for versioned prompt templates. */
@Service
public class PromptTemplateService {

  private final PromptTemplateRepository repository;

  public PromptTemplateService(PromptTemplateRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public PromptTemplate createNextVersion(String code, String template, String description) {
    String tenantId = TenantContext.tenant();
    int nextVersion =
        repository.findByTenantIdAndCodeOrderByVersionDesc(tenantId, code).stream()
                .findFirst()
                .map(PromptTemplate::getVersion)
                .orElse(0)
            + 1;
    PromptTemplate entity = new PromptTemplate(code, nextVersion, template);
    entity.setDescription(description);
    entity.setTenantId(tenantId);
    return repository.save(entity);
  }

  @Transactional
  public PromptTemplate publish(String id) {
    PromptTemplate entity =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("prompt template not found: " + id));
    entity.setPublished(true);
    return entity;
  }

  @Transactional(readOnly = true)
  public Optional<PromptTemplate> latestPublished(String code) {
    return repository.findFirstByTenantIdAndCodeAndPublishedTrueOrderByVersionDesc(
        TenantContext.tenant(), code);
  }

  @Transactional(readOnly = true)
  public List<PromptTemplate> history(String code) {
    return repository.findByTenantIdAndCodeOrderByVersionDesc(TenantContext.tenant(), code);
  }

  @Transactional(readOnly = true)
  public List<PromptTemplate> listAll() {
    return repository.findByTenantIdOrderByCodeAscVersionDesc(TenantContext.tenant());
  }

  /** Substitute {@code {{key}}} placeholders with values from {@code variables}. */
  public static String render(String template, Map<String, Object> variables) {
    if (template == null || variables == null || variables.isEmpty()) {
      return template;
    }
    String result = template;
    for (Map.Entry<String, Object> entry : variables.entrySet()) {
      String placeholder = "{{" + entry.getKey() + "}}";
      String value = entry.getValue() == null ? "" : String.valueOf(entry.getValue());
      result = result.replace(placeholder, value);
    }
    return result;
  }
}
