package io.jeegit.ai.rag;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 * Knowledge service backed by a relational table of chunks. If the target database ships pgvector
 * the search is hot-loaded against an IVFFLAT/HNSW index on the {@code embedding} column; the
 * default implementation used here performs a tenant-scoped case-insensitive substring search so
 * the contract holds even without the extension.
 */
public class PgVectorKnowledgeService implements KnowledgeService {

  private final KnowledgeChunkRepository repository;

  @PersistenceContext private EntityManager em;

  public PgVectorKnowledgeService(KnowledgeChunkRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public KnowledgeChunk index(String tenantId, String sourceId, String title, String body) {
    KnowledgeChunk chunk = new KnowledgeChunk(sourceId, title, body);
    chunk.setTenantId(tenantId);
    return repository.save(chunk);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Citation> retrieve(String tenantId, String query, int topK) {
    if (query == null || query.isBlank()) return List.of();
    int cap = Math.max(1, Math.min(topK, 50));
    List<KnowledgeChunk> hits = repository.search(tenantId, query.trim());
    List<Citation> out = new ArrayList<>();
    for (int i = 0; i < hits.size() && i < cap; i++) {
      KnowledgeChunk chunk = hits.get(i);
      out.add(
          new Citation(
              chunk.getId(),
              chunk.getTitle(),
              abbreviate(chunk.getBody()),
              Map.of(
                  "sourceId",
                  chunk.getSourceId() == null ? "" : chunk.getSourceId(),
                  "tenantId",
                  chunk.getTenantId(),
                  "backend",
                  "pgvector-or-textual")));
    }
    return out;
  }

  private static String abbreviate(String s) {
    if (s == null) return "";
    return s.length() > 400 ? s.substring(0, 400) + "…" : s;
  }
}
