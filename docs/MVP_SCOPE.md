# MVP 范围 MVP_SCOPE · jeegit

> 首版 MVP 只做 **一个闭环**：**事项受理 → 自动分派 → 人工审批 → 结果通知**。
> 原则：能跑通治理闭环 > 功能数量。

---

## 1. 首发场景（只选 1 个）

**政府事项受理与审批督办**（中小企业同样可用于内部审批 / 工单）。

场景脚本：

1. 申请人提交一个 "事项申请"（含标题、类别、描述、附件）。
2. **受理分派 Agent** 根据规则 + 分类结果自动分派给合适部门。
3. Agent 给出可解释的分派理由（写入审计）。
4. 高风险/跨部门事项 → 命中 **HITL 策略** → 人工审核后才真正分派。
5. 审批通过后写入结果 + 发送通知。

## 2. MVP 必须有的能力（Must）

| 能力                 | 所在模块                  |
| -------------------- | ------------------------- |
| 多租户隔离           | `jeegit-tech`             |
| 用户 / 角色 / 权限   | `jeegit-tech`             |
| 审计日志（append-only） | `jeegit-tech`           |
| 事项（Matter）实体与 REST API | `jeegit-business` |
| 分派审批流骨架（接入点） | `jeegit-business`     |
| Agent Runtime 最小版 | `jeegit-ai`               |
| ModelGateway（抽象 + Echo 实现） | `jeegit-ai`   |
| ToolRegistry / Tool 调用 | `jeegit-ai`           |
| HITL 策略拦截        | `jeegit-ai`               |
| 受理分派示范 Agent   | `jeegit-agent-intake`     |
| 开放平台 API 入口（v1） | `jeegit-openapi`       |
| 启动器 + Actuator 健康检查 | `jeegit-bootstrap`  |

## 3. MVP **不做** 的（Won't）

- ❌ 真实大模型调用（用 Echo/Stub 模型做端到端验证）
- ❌ 真实向量库集成
- ❌ 真实 BPMN 引擎（只保留接入点接口）
- ❌ 前端 UI（先保证后端能力完整）
- ❌ 多数据库方言适配（MVP 只保证 H2 内嵌启动 + PostgreSQL 可切换）
- ❌ 插件市场 / 开发者门户 UI

## 4. 验收标准

1. `mvn -q -DskipTests package` 成功编译。
2. `java -jar jeegit-bootstrap/target/*.jar` 启动成功。
3. `GET /actuator/health` 返回 `UP`。
4. `GET /api/v1/platform/info` 返回平台元信息与模块清单。
5. `POST /api/v1/matters` 可创建事项。
6. `POST /api/v1/matters/{id}/dispatch` 触发受理分派 Agent，返回：
   - 分派决策
   - 可解释推理摘要
   - 审计记录 ID
   - （若命中 HITL）返回 `status=PENDING_APPROVAL`
7. `audit_log` 中能查到完整调用链。

## 5. 下一步（Out of MVP）

- 接入真实 BPMN（Camunda / Flowable）
- 接入真实模型（OpenAI 兼容 / 本地 SLM）
- RAG 知识库（政策法规检索）
- 开发者门户 + API Key 管理
- 前端门户与工作台
