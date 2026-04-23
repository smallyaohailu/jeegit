# jeegit · نظرة عامة على المنتج (العربية)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md)

**jeegit** إطار عمل للتطبيقات أصيل للذكاء الاصطناعي، مبني على Java 21 و Spring Boot 3 و JPA،
ويُنشر تحت ترخيص Apache-2.0. يوفّر أساسًا موحّدًا لبناء تطبيقات مؤسسية متعددة المستأجرين
قابلة للتدقيق وذات قدرات ذكاء اصطناعي — من أدوات سير العمل الداخلية إلى الخدمات الحكومية
الحساسة.

## ما هو متاح فورًا

- **نواة المنصة**: تعدد المستأجرين، شجرة المنظمات، قواميس، أدوار + نطاقات البيانات،
  سجل تدقيق للإضافة فقط، تحديد معدّل الطلبات.
- **نواة الذكاء الاصطناعي**: بوابة نماذج (تتضمن تنفيذًا متوافقًا مع OpenAI)،
  زمن تشغيل للوكلاء، سجل أدوات، حارس HITL، إدارة إصدارات قوالب النصوص،
  خدمة معرفة جاهزة لـ pgvector، منصة تقييم.
- **المنصة المفتوحة**: REST + OpenAPI 3 و Swagger UI ومفاتيح API ببصمة SHA-256
  وفلتر مصادقة الشركاء.
- **الواجهة الإدارية**: تطبيق صفحة واحدة بتصميم Material Design 3 يُقدَّم من Spring Boot،
  ومُرفق به 12 لغة (دعم الاتجاه من اليمين إلى اليسار للعربية).
- **التشغيل**: PostgreSQL + Flyway، Docker متعدد المراحل، docker-compose،
  مقاييس Prometheus، تتبع OpenTelemetry، CI على GitHub Actions.

## بدء سريع

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# أو تشغيل الحزمة الكاملة مع PostgreSQL عبر docker compose
docker compose up --build
```

افتح <http://localhost:8080/>. تتفاوض الواجهة على اللغة عبر `Accept-Language` وتقبل
المعامل `?lang=xx` للاختبار، وتُرجع كل استجابة REST اللغة المختارة في `meta.locale`.

## مزيد من القراءة

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)
