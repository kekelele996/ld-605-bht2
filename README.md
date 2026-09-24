# 城市水务漏损巡检平台

面向水务公司的管网资产、漏损上报、巡检任务和维修闭环管理系统。

## 快速启动

```bash
cp .env.example .env && docker compose up -d
```

## 访问地址或 CLI 示例

前端：<http://localhost:20105>

后端健康检查：<http://localhost:21105/health>


## 本地开发方式

- 前端：`cd frontend && npm install && npm run dev`
- 后端：进入 `backend` 后按技术栈运行开发命令，接口统一挂在 `/api`。


## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | React 18 + TypeScript + Vite + Ant Design + ECharts + Zustand |
| 后端 | Spring Boot 3 + Java 17 + JPA |
| 数据库 | PostgreSQL 15 |
| 部署 | Docker Compose |

## 项目目录结构

```text
frontend/src/api, stores, types, constants, constructors, components/common, hooks, pages, router, utils, mocks
backend/src/routes, controllers, services, models, repositories, middlewares, constants, constructors, utils, types, config
```

## 环境变量说明

- `COMPOSE_PROJECT_NAME`: Compose 项目名，默认 `water-leak`
- `FRONTEND_PORT`: 前端端口，默认 `20105`
- `BACKEND_PORT`: 后端端口，默认 `21105`
- `DB_PORT`: 数据库宿主机端口
- `DB_USER/DB_PASSWORD/DB_NAME`: 本地数据库凭据

## Docker 部署说明

- 根 Compose 文件不写 `version`，顶层 `name: water-leak`。
- 容器名均使用 `${COMPOSE_PROJECT_NAME:-water-leak}` 前缀。
- 数据库使用命名卷，避免绑定中文路径。
- 常见问题：端口占用时修改 `.env` 中端口后重启；需要重置数据时执行 `docker compose down -v`。

## 枚举/常量出现位置清单

- LeakLevel: constants/LeakLevel、types/LeakLevel、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- RepairStatus: constants/RepairStatus、types/RepairStatus、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
- RiskLevel: constants/RiskLevel、types/RiskLevel、constructors、logTemplates、errorMessages、筛选器、展示组件/控制器均有引用。
  - 后端：`constants/RiskLevel.java`（含 `getCycleDays()` 周期映射与 `isLowerThan` 下调判定）、`constants/PointStatus.java`（点位启停）、`constants/VerifyStatus.java`（待核实判定）、`constants/ErrorCodes.java`、`constants/ErrorMessages.java`、`constants/LogTemplates.java`、`utils/InspectionScheduleCalculator.java`、`services/PipelineSegmentService.java`、`controllers/PipelineSegmentController.java`。
  - 前端：`constants/RiskLevel.ts` 与 `types/RiskLevel.ts`（均含 `RISK_CYCLE_DAYS`/`RiskLevelText`）、`constants/errorCodes.ts`、`constants/errorMessages.ts`、`constants/logTemplates.ts`、`utils/formatters.ts`、`api/PipelineSegment.ts`、`stores/PipelineSegmentStore.ts`、`pages/PipelinesPage.tsx`、`components/common/ScheduleResultPanel.tsx`、`components/common/RiskBadge.tsx`。

## 风险调整与巡检周期联动

班组长在「管网资产」页调整并保存管段风险时，风险与巡检周期在后端同一次请求内联动：

| 风险等级 | 巡检周期 |
|---|---|
| 低风险 LOW | 30 天 |
| 中风险 MEDIUM | 14 天 |
| 高风险 HIGH | 7 天 |
| 极高风险 EXTREME | 3 天 |

- 起点取该点位**最近一次检查时间** `last_checked_at`，新到期日 `next_due_at = last_checked_at + 周期`；仅 `ENABLED`（启用）点位参与重排，停用点位周期与到期日不变，也不出现在清单中。
- 管段存在 **待核实（PENDING_VERIFY）漏损** 时，风险**不允许下降**：后端返回 `409 RISK_DOWNGRADE_BLOCKED`，**原风险与原巡检周期全部保留**；风险上调或无待核实漏损时正常保存。
- 保存接口：`PUT /api/pipeline-segment/risk`，请求体 `{id, risk_level}`，成功响应含新风险、新周期及各启用点位的新到期日清单（`enabledPoints`）；前端保存成功后在页面下方列出各启用点位的新到期日，后端拒绝时本地不清空、保留原风险与原周期并提示原因。
- 校验类拒绝：风险值非法返回 `400 INVALID_RISK_LEVEL`，管段不存在返回 `404 SEGMENT_NOT_FOUND`。


## 为什么会牵一发动全身

实体字段、枚举、日志模板、错误消息、构造器、筛选器和展示组件被刻意拆散到多个目录；修改一个状态值通常需要同步类型、构造器、服务、控制器、store、页面、README 与数据库种子。

## License

MIT
