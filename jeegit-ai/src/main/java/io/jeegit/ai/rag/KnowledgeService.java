package io.jeegit.ai.rag;

import java.util.List;
import java.util.Map;

/**
 * 知识检索（RAG）接入点。MVP 阶段仅定义契约，
 * 实际向量库/语义检索作为独立子系统在后续版本接入。
 * 架构宪章第 5 条：JPA 不承担向量检索。
 */
public interface KnowledgeService {

    record Citation(String id, String title, String snippet, Map<String, Object> meta) {
    }

    List<Citation> retrieve(String tenantId, String query, int topK);
}
