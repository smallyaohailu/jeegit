# AI 治理 AI_GOVERNANCE · jeegit

> 本文定义 jeegit 平台 **AI 能力的治理框架**：权限、审计、人机协同、评测、合规。
> 适用对象：所有运行在平台内的 Agent、模型调用、RAG 流程、工具调用。

---

## 1. 核心原则

1. **可解释** — 每一次智能决策必须产出可读的推理摘要与依据。
2. **可审计** — 全链路留痕、不可篡改、可回放。
3. **可控制** — 高风险动作必须人工确认，随时可中断、可回退。
4. **可评测** — 模型 / Prompt / 知识变更必须跑评测集再上线。
5. **租户隔离** — 模型配置 / 知识库 / Prompt / 日志必须按租户隔离。

## 2. Agent 权限模型

每个 Agent 都是**一等公民主体**，具备：

| 字段            | 含义                                                           |
| --------------- | -------------------------------------------------------------- |
| `agentId`       | 全局唯一标识                                                   |
| `tenantId`      | 所属租户                                                       |
| `ownerId`       | 责任人（用于问责）                                             |
| `roles`         | 拥有的角色（复用 IAM 角色体系）                                |
| `allowedTools`  | 允许调用的工具白名单                                           |
| `riskLevel`     | LOW / MEDIUM / HIGH                                            |
| `hitlPolicy`    | 人机协同策略：NONE / ON_HIGH_RISK / ALWAYS                     |
| `budget`        | Token / 金额 / 调用次数配额                                    |

**权限最小化**：Agent 默认无任何工具权限，必须显式授予。

## 3. 风险分级与人机协同（HITL）

| 风险级别 | 示例                                   | 必须措施                         |
| -------- | -------------------------------------- | -------------------------------- |
| LOW      | 查询、只读问答                         | 记录调用链                       |
| MEDIUM   | 创建工单、生成建议                     | 记录 + 用户可回滚                |
| HIGH     | 修改配置、发起审批、对外通知           | **必须人工确认**（HITL 阻塞）    |

- 人工确认产生 `ApprovalRecord`，包含确认人、时间、理由。
- 未经确认的 HIGH 动作必须被 Runtime **硬阻断**。

## 4. 审计（Audit by Default）

所有 Agent 行为写入 **`audit_log`**（JPA 实体），含：

- `traceId` / `spanId`（OpenTelemetry 对齐）
- `tenantId` / `agentId` / `actorId`
- `action`（`MODEL_CALL` / `TOOL_CALL` / `RAG_QUERY` / `HITL_APPROVE` …）
- `inputHash` / `outputHash`（敏感内容哈希，可选留原文）
- `riskLevel` / `decision`（ALLOW / DENY / APPROVED / REJECTED）
- `latencyMs` / `tokenIn` / `tokenOut` / `costEstimate`
- `reasoningSummary`（可读摘要，用于解释）

审计日志 **仅追加 (append-only)**，不允许更新/删除。

## 5. 模型调用治理

- 所有模型调用 **必须** 走 **ModelGateway**；禁止业务代码直接调用任何厂商 SDK。
- 网关统一负责：鉴权、限流、成本计量、审计、PII 脱敏、回退路由。
- 模型配置按租户隔离：`tenant → modelProvider → model → promptTemplate`。

## 6. Prompt 资产化

- Prompt 不是字符串，是 **可版本化配置**：模板、参数、权限、审计、回滚。
- 每次变更产生新版本号，生产环境使用固定版本，禁止"热改字符串"。

## 7. 评测即门禁（Eval as Gate）

- 每个 Agent / Prompt / 知识库都必须绑定 **评测集**（可小，但必须有）。
- CI 在合并前运行评测，不达阈值的变更 **禁止入主干**。
- 指标至少覆盖：准确率、幻觉率、时延 P95、成本/次。

## 8. 合规与安全

- 数据出境、跨租户访问、对外通知等 **默认拒绝**，需策略显式放行。
- 敏感数据（身份证、手机号、银行卡号）在日志与 Prompt 中强制脱敏。
- 政务场景默认要求 **可离线 / 私有化部署**。

## 9. 责任归属

- Agent 的最终责任人 = `ownerId`（通常是业务责任人，而非开发者）。
- 平台不代替业务方承担决策责任；平台负责**能力可用性 + 审计留痕**。

## 10. 发布与回退

- Agent 发布采用 **灰度 + A/B** 策略，按租户或百分比放量。
- 每次发布必须可一键回退到上一个稳定版本。
