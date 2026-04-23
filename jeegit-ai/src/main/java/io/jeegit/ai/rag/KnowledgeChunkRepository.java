package io.jeegit.ai.rag;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunk, String> {

  /**
   * Plain full-text fallback when no vector backend is configured: case-insensitive substring
   * match, capped via {@code LIMIT} in the calling service.
   */
  @Query(
      "SELECT k FROM KnowledgeChunk k WHERE k.tenantId = :tenantId "
          + "AND (LOWER(k.title) LIKE LOWER(CONCAT('%', :q, '%')) "
          + "OR LOWER(k.body) LIKE LOWER(CONCAT('%', :q, '%')))")
  List<KnowledgeChunk> search(@Param("tenantId") String tenantId, @Param("q") String q);
}
