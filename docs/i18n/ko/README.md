# jeegit · 제품 개요 (한국어)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit**은 Java 21 / Spring Boot 3 / JPA 기반의 **AI 네이티브 엔터프라이즈 애플리케이션 프레임워크**입니다.
Apache-2.0 라이선스로 공개되며, 멀티테넌트·감사·AI를 내장한 업무 애플리케이션을 하나의 기반 위에서 구축할 수 있습니다.

## 기본 탑재 기능

- **플랫폼 코어**: 멀티테넌트, 조직 트리, 딕셔너리, 역할 + 데이터 스코프, append-only 감사, 요청 제한.
- **AI 코어**: 모델 게이트웨이(OpenAI 호환 구현 포함), 에이전트 런타임, 툴 레지스트리, HITL 가드,
  프롬프트 템플릿 버전 관리, pgvector 준비된 지식 서비스, 평가 하네스.
- **오픈 플랫폼**: REST + OpenAPI 3, Swagger UI, SHA-256 다이제스트 API 키, 파트너 인증 필터.
- **콘솔**: Spring Boot가 제공하는 Material Design 3 SPA. 12개 언어 내장(아랍어 RTL 지원).
- **운영**: PostgreSQL + Flyway, Docker 멀티스테이지 이미지, docker-compose, Prometheus 메트릭,
  OpenTelemetry 트레이싱, GitHub Actions CI.

## 빠른 시작

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# 또는 docker compose 로 PostgreSQL 포함 풀스택 실행
docker compose up --build
```

<http://localhost:8080/>에 접속하세요. `Accept-Language` 기반 언어 협상, `?lang=xx` 테스트용 오버라이드,
모든 REST 응답의 `meta.locale`에 선택된 언어 태그가 포함됩니다.

## 더 읽을거리

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)
