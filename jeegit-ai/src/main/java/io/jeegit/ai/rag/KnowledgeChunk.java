package io.jeegit.ai.rag;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Knowledge snippet — a plain-text chunk that has optionally been indexed in a vector store.
 *
 * <p>The entity itself does not carry the vector embedding; those live in the dedicated vector
 * table ({@code jg_knowledge_chunk_embedding}) managed by {@link PgVectorKnowledgeService}.
 */
@Entity
@Table(
    name = "jg_knowledge_chunk",
    indexes = {
      @Index(name = "idx_knowledge_tenant", columnList = "tenant_id"),
      @Index(name = "idx_knowledge_source", columnList = "tenant_id,source_id")
    })
public class KnowledgeChunk extends TenantAwareEntity {

  @Column(name = "source_id", length = 128)
  private String sourceId;

  @Column(name = "title", length = 500)
  private String title;

  @Column(name = "body", nullable = false, length = 100_000)
  private String body;

  @Column(name = "meta_json", length = 4000)
  private String metaJson;

  public KnowledgeChunk() {}

  public KnowledgeChunk(String sourceId, String title, String body) {
    this.sourceId = sourceId;
    this.title = title;
    this.body = body;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getMetaJson() {
    return metaJson;
  }

  public void setMetaJson(String metaJson) {
    this.metaJson = metaJson;
  }
}
