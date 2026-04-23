# jeegit · プロダクト概要（日本語）

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** は Java 21 / Spring Boot 3 / JPA をベースとした **AI ネイティブなエンタープライズ・アプリケーション・フレームワーク** です。
Apache-2.0 ライセンスで公開されており、社内ワークフローから基幹業務の電子行政システムまで、
マルチテナント・監査対応・AI 駆動のアプリケーションを一貫した基盤で構築できます。

## 同梱される機能

- **プラットフォーム・コア**: マルチテナント、組織ツリー、辞書、ロール + データスコープ、追記専用の監査、レート制限。
- **AI コア**: モデル・ゲートウェイ（OpenAI 互換実装を同梱）、エージェント・ランタイム、ツール・レジストリ、
  HITL ガード、プロンプト・テンプレートのバージョン管理、pgvector 対応ナレッジサービス、評価ハーネス。
- **オープン・プラットフォーム**: REST + OpenAPI 3、Swagger UI、SHA-256 ダイジェストの API キー、
  パートナー認証フィルタ。
- **コンソール**: Spring Boot から配信される Material Design 3 の SPA。12 言語を同梱（アラビア語は RTL 対応）。
- **運用**: PostgreSQL + Flyway、Docker マルチステージ・イメージ、docker-compose、Prometheus メトリクス、
  OpenTelemetry トレーシング、GitHub Actions CI。

## クイックスタート

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# または docker compose で PostgreSQL を含むフルスタック起動
docker compose up --build
```

<http://localhost:8080/> を開いてください。コンソールは `Accept-Language` に従って言語をネゴシエートし、
テスト用に `?lang=xx` もサポートします。すべての REST レスポンスは `meta.locale` で選択された言語を返します。

## 参考ドキュメント

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)
