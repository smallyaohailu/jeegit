package io.jeegit.ai.rag;

import java.util.List;
import java.util.Map;

/**
 * Retrieval contract for RAG providers. The preview uses a no-op provider; production deployments
 * plug in pgvector, Milvus, Elasticsearch, or a vendor service by registering a {@code
 * KnowledgeService} bean. The Architecture Charter (§5) requires this to stay out of JPA's main
 * transactional path.
 */
public interface KnowledgeService {

  record Citation(String id, String title, String snippet, Map<String, Object> meta) {}

  List<Citation> retrieve(String tenantId, String query, int topK);
}
