-- Knowledge / RAG chunk store (added in 1.1.0)
-- When the pgvector extension is available, add an embedding column and an
-- HNSW index after enabling it:
--   CREATE EXTENSION IF NOT EXISTS vector;
--   ALTER TABLE jg_knowledge_chunk ADD COLUMN embedding vector(1536);
--   CREATE INDEX idx_knowledge_chunk_embedding_hnsw
--       ON jg_knowledge_chunk USING hnsw (embedding vector_cosine_ops);

CREATE TABLE jg_knowledge_chunk (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    source_id   VARCHAR(128),
    title       VARCHAR(500),
    body        TEXT         NOT NULL,
    meta_json   VARCHAR(4000)
);
CREATE INDEX idx_knowledge_tenant ON jg_knowledge_chunk (tenant_id);
CREATE INDEX idx_knowledge_source ON jg_knowledge_chunk (tenant_id, source_id);
