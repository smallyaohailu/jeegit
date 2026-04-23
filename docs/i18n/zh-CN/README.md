# jeegit · 产品概览（简体中文）

[English](../en/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** 是基于 Java 21、Spring Boot 3 与 JPA 的 **AI 原生企业级应用框架**，以 Apache-2.0 协议开源。
用作构建多租户、可审计、具备 AI 能力的业务应用的统一底座——覆盖从内部工作流到关键政务系统的场景。

## 平台内置能力

- **平台内核**：多租户、组织树、字典服务、角色 + 数据范围、只追加审计、限流。
- **AI 原生底座**：模型网关（内置 OpenAI 兼容实现）、Agent 运行时、工具注册中心、人机协同（HITL）闸门、
  Prompt 模板版本化、面向 pgvector 的知识服务、评测流水线。
- **开放平台**：REST + OpenAPI 3、Swagger UI、基于 SHA-256 摘要的 API Key、伙伴认证过滤器。
- **管理控制台**：由 Spring Boot 内嵌提供的 Material Design 3 单页应用，内置 12 种语言（支持阿拉伯语的 RTL 布局）。
- **运维就绪**：PostgreSQL + Flyway、Docker 多阶段镜像、docker-compose、Prometheus 指标、
  OpenTelemetry 链路、GitHub Actions CI。

## 快速开始

```bash
# 预览（使用内存 H2，零外部依赖）
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar

# 生产形态（PostgreSQL + Flyway，通过 docker-compose 启动）
docker compose up --build
```

访问 <http://localhost:8080/>。控制台会根据 `Accept-Language` 协商语言，也接受 `?lang=xx` 用于测试；
每一次 REST 响应都会在 `meta.locale` 字段回显实际使用的语言。

## 进一步阅读

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md) — 产品范围、目标用户、不做事项
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md) — 不可变架构原则
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md) — 权限、审计、HITL、评测门禁
- [`LANGUAGES.md`](../LANGUAGES.md) — 十二种语言的契约
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)
