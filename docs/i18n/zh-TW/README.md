# jeegit · 產品概覽（繁體中文）

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** 是以 Java 21、Spring Boot 3 與 JPA 構建的 **AI 原生企業級應用框架**，採 Apache-2.0 授權。
作為多租戶、可稽核、具 AI 能力之業務應用的統一基座——涵蓋從內部工作流到關鍵政務系統的情境。

## 平台內建能力

- **平台核心**：多租戶、組織樹、字典服務、角色與資料範圍、僅追加稽核、限流。
- **AI 原生底座**：模型閘道（內建 OpenAI 相容實作）、Agent 執行時、工具註冊、HITL 閘門、
  Prompt 範本版本化、pgvector 就緒的知識服務、評測流水線。
- **開放平台**：REST + OpenAPI 3、Swagger UI、SHA-256 摘要 API Key、夥伴驗證篩選器。
- **管理控制台**：由 Spring Boot 提供的 Material Design 3 單頁應用，內建 12 種語言（阿拉伯語 RTL 支援）。
- **運維**：PostgreSQL + Flyway、Docker 多階段映像、docker-compose、Prometheus 指標、
  OpenTelemetry 追蹤、GitHub Actions CI。

## 快速開始

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# 或透過 docker compose 啟動整套 PostgreSQL 生產形態
docker compose up --build
```

接著打開 <http://localhost:8080/>。控制台會依 `Accept-Language` 協商語言，並接受 `?lang=xx` 覆寫；
每次 REST 回應都會於 `meta.locale` 欄位回報實際語言。

## 延伸閱讀

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)
