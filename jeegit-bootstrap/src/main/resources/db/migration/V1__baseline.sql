-- jeegit schema baseline (V1.0)
-- Shared shape of every auditable row:
--   id (UUID string) PK
--   del_flag, status (string enums)
--   remarks, created_by, created_at, updated_by, updated_at
-- Tenant-aware rows add a tenant_id column.

CREATE TABLE jg_tenant (
    id          VARCHAR(64)  PRIMARY KEY,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    code        VARCHAR(64)  NOT NULL UNIQUE,
    name        VARCHAR(128) NOT NULL,
    description VARCHAR(500),
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE jg_org (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    parent_id   VARCHAR(64),
    parent_ids  VARCHAR(2000),
    tree_level  INTEGER      NOT NULL,
    tree_sort   INTEGER      NOT NULL,
    tree_leaf   BOOLEAN      NOT NULL,
    code        VARCHAR(64)  NOT NULL,
    name        VARCHAR(200) NOT NULL,
    type        VARCHAR(32)  NOT NULL
);
CREATE INDEX idx_org_tenant  ON jg_org (tenant_id);
CREATE UNIQUE INDEX idx_org_code ON jg_org (tenant_id, code);
CREATE INDEX idx_org_parent  ON jg_org (parent_id);

CREATE TABLE jg_user (
    id            VARCHAR(64)  PRIMARY KEY,
    tenant_id     VARCHAR(64)  NOT NULL,
    del_flag      VARCHAR(16)  NOT NULL,
    status        VARCHAR(16)  NOT NULL,
    remarks       VARCHAR(500),
    created_by    VARCHAR(128),
    created_at    TIMESTAMPTZ,
    updated_by    VARCHAR(128),
    updated_at    TIMESTAMPTZ,
    username      VARCHAR(128) NOT NULL,
    display_name  VARCHAR(256),
    org_id        VARCHAR(64),
    email         VARCHAR(256),
    enabled       BOOLEAN      NOT NULL
);
CREATE UNIQUE INDEX uk_user_name ON jg_user (tenant_id, username);
CREATE INDEX idx_user_org ON jg_user (tenant_id, org_id);

CREATE TABLE jg_role (
    id              VARCHAR(64)  PRIMARY KEY,
    tenant_id       VARCHAR(64)  NOT NULL,
    del_flag        VARCHAR(16)  NOT NULL,
    status          VARCHAR(16)  NOT NULL,
    remarks         VARCHAR(500),
    created_by      VARCHAR(128),
    created_at      TIMESTAMPTZ,
    updated_by      VARCHAR(128),
    updated_at      TIMESTAMPTZ,
    code            VARCHAR(64)  NOT NULL,
    name            VARCHAR(200) NOT NULL,
    data_scope      VARCHAR(32)  NOT NULL,
    custom_org_ids  VARCHAR(2000)
);
CREATE UNIQUE INDEX uk_role_code ON jg_role (tenant_id, code);

CREATE TABLE jg_dict_type (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    code        VARCHAR(64)  NOT NULL,
    name        VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    system_flag BOOLEAN      NOT NULL
);
CREATE UNIQUE INDEX uk_dict_type_code ON jg_dict_type (tenant_id, code);

CREATE TABLE jg_dict_item (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    type_code   VARCHAR(64)  NOT NULL,
    item_key    VARCHAR(128) NOT NULL,
    item_label  VARCHAR(200) NOT NULL,
    item_value  VARCHAR(500),
    sort_order  INTEGER      NOT NULL,
    attributes  VARCHAR(4000)
);
CREATE INDEX idx_dict_item_type ON jg_dict_item (tenant_id, type_code);
CREATE UNIQUE INDEX uk_dict_item_kv ON jg_dict_item (tenant_id, type_code, item_key);

CREATE TABLE jg_matter (
    id                   VARCHAR(64)  PRIMARY KEY,
    tenant_id            VARCHAR(64)  NOT NULL,
    del_flag             VARCHAR(16)  NOT NULL,
    status               VARCHAR(16)  NOT NULL,
    remarks              VARCHAR(500),
    created_by           VARCHAR(128),
    created_at           TIMESTAMPTZ,
    updated_by           VARCHAR(128),
    updated_at           TIMESTAMPTZ,
    title                VARCHAR(200) NOT NULL,
    category             VARCHAR(64),
    description          VARCHAR(4000),
    applicant_id         VARCHAR(128),
    org_id               VARCHAR(64),
    assigned_department  VARCHAR(128),
    matter_status        VARCHAR(32)  NOT NULL
);
CREATE INDEX idx_matter_tenant ON jg_matter (tenant_id);
CREATE INDEX idx_matter_status ON jg_matter (matter_status);
CREATE INDEX idx_matter_org    ON jg_matter (tenant_id, org_id);

CREATE TABLE jg_audit_log (
    id                VARCHAR(64) PRIMARY KEY,
    tenant_id         VARCHAR(64) NOT NULL,
    trace_id          VARCHAR(64),
    agent_id          VARCHAR(64),
    actor_id          VARCHAR(128),
    action            VARCHAR(64) NOT NULL,
    risk_level        VARCHAR(16),
    decision          VARCHAR(32),
    reasoning_summary VARCHAR(4000),
    input_digest      VARCHAR(2000),
    output_digest     VARCHAR(2000),
    latency_ms        BIGINT,
    token_in          BIGINT,
    token_out         BIGINT,
    occurred_at       TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_audit_tenant ON jg_audit_log (tenant_id);
CREATE INDEX idx_audit_trace  ON jg_audit_log (trace_id);
CREATE INDEX idx_audit_agent  ON jg_audit_log (agent_id);

CREATE TABLE jg_api_key (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    key_digest  VARCHAR(128) NOT NULL,
    name        VARCHAR(200) NOT NULL,
    owner       VARCHAR(128),
    enabled     BOOLEAN      NOT NULL
);
CREATE UNIQUE INDEX uk_api_key_digest ON jg_api_key (key_digest);
CREATE INDEX idx_api_key_tenant ON jg_api_key (tenant_id);

CREATE TABLE jg_prompt_template (
    id          VARCHAR(64)  PRIMARY KEY,
    tenant_id   VARCHAR(64)  NOT NULL,
    del_flag    VARCHAR(16)  NOT NULL,
    status      VARCHAR(16)  NOT NULL,
    remarks     VARCHAR(500),
    created_by  VARCHAR(128),
    created_at  TIMESTAMPTZ,
    updated_by  VARCHAR(128),
    updated_at  TIMESTAMPTZ,
    code        VARCHAR(128) NOT NULL,
    version     INTEGER      NOT NULL,
    template    VARCHAR(8000) NOT NULL,
    model_key   VARCHAR(128),
    description VARCHAR(500),
    published   BOOLEAN      NOT NULL
);
CREATE UNIQUE INDEX uk_prompt_tenant_code_ver ON jg_prompt_template (tenant_id, code, version);
CREATE INDEX idx_prompt_tenant_code ON jg_prompt_template (tenant_id, code);
