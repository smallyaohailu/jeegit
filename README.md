# jeegit

**jeegit** 是一个面向政府数字化转型与中小企业数字化的 **AI-Native 企业级快速开发平台框架**，
采用 **Apache License 2.0** 纯开源免费发布，以 **Java 21 + Spring Boot 3 + JPA** 为技术主线。

> 我们的愿景：成为中国本土、企业级、长期可持续的 **纯开源免费** 政企开发平台。

---

## 核心定位

- 面向政府：合规、审计、流程治理、跨部门协同、可私有化/国产化部署。
- 面向中小企业：低门槛、开箱模板、成本可控、二次开发友好。
- 面向开发者：标准化、插件化、可二开、生态可扩展。

## 架构全景（1 + 4 + 1）

```
┌─────────────────────────────────────────────────────────────┐
│                     开放平台（API + Event）                 │
│   API Gateway · 开发者门户 · API 资产目录 · SDK 生成        │
└─────────────────────────────────────────────────────────────┘
┌─────────────┬─────────────┬─────────────┬─────────────────┐
│  数据中台   │  业务中台   │  技术中台   │   应用中台      │
│  主数据/    │  事项/工单/ │  IAM/租户/  │  门户/BFF/      │
│  指标/标签  │  合同/审批  │  审计/规则  │  模板工厂       │
└─────────────┴─────────────┴─────────────┴─────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                     AI 原生底座（AI Core）                  │
│  Model Gateway · Agent Runtime · Tool Registry · RAG        │
│  EvalOps · Human-in-the-Loop · AI Governance                │
└─────────────────────────────────────────────────────────────┘
```

## 地基文档（必读）

- [`docs/PRODUCT_CHARTER.md`](docs/PRODUCT_CHARTER.md) — 产品章程：愿景、边界、目标用户。
- [`docs/ARCHITECTURE_CHARTER.md`](docs/ARCHITECTURE_CHARTER.md) — 架构宪章：不可变原则。
- [`docs/AI_GOVERNANCE.md`](docs/AI_GOVERNANCE.md) — AI 治理：权限、审计、人机协同。
- [`docs/MVP_SCOPE.md`](docs/MVP_SCOPE.md) — 首版 MVP 范围。
- [`docs/MODEL_LICENSES.md`](docs/MODEL_LICENSES.md) — 模型许可政策。
- [`docs/DATA_POLICY.md`](docs/DATA_POLICY.md) — 数据政策。
- [`docs/AI_SAFETY_POLICY.md`](docs/AI_SAFETY_POLICY.md) — AI 安全策略。

## 模块结构

| 模块                     | 层级          | 说明                                                |
| ------------------------ | ------------- | --------------------------------------------------- |
| `jeegit-common`          | 通用          | 基础类、上下文、异常、通用响应                      |
| `jeegit-tech`            | 技术中台      | 租户、组织、用户、角色、权限、审计（JPA 实体）      |
| `jeegit-data`            | 数据中台      | 主数据、指标、标签骨架                              |
| `jeegit-business`        | 业务中台      | 事项受理、工单、审批（首个行业模板）                |
| `jeegit-app`             | 应用中台      | 门户、BFF、场景装配                                 |
| `jeegit-ai`              | AI 原生底座   | ModelGateway / AgentRuntime / ToolRegistry / RAG    |
| `jeegit-openapi`         | 开放平台      | API Gateway 接入点、开发者门户骨架                  |
| `jeegit-agent-intake`    | 示范 Agent    | 受理分派 Agent（可解释 / 可审计 / 可人工接管）      |
| `jeegit-bootstrap`       | 启动器        | Spring Boot 3 主应用，组装所有模块                  |

## 快速开始

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap-*.jar
# 访问
curl http://127.0.0.1:8080/actuator/health
curl http://127.0.0.1:8080/api/v1/platform/info
```

## License

Apache License 2.0 — 代码能力不阉割，付费的是服务质量和交付效率。
