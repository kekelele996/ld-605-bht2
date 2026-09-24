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
- VerifyStatus（漏损核实状态 PENDING/CONFIRMED/DISMISSED）: backend `constants/VerifyStatus.java`、frontend `constants/VerifyStatus.ts`，被 PipelineSegmentService 与 mocks/riskScheduleMock 引用。
- PointStatus（巡检点启停 ENABLED/DISABLED）: backend `constants/PointStatus.java`、frontend `constants/PointStatus.ts`，决定哪些点位参与周期重算。

## 风险等级与巡检周期联动

管网页（`/pipelines`）保存管段风险后，巡检周期立即联动，起点取各启用点位最近一次检查时间：

| 风险等级 | 巡检周期 |
|---|---|
| LOW 低 | 30 天 |
| MEDIUM 中 | 14 天 |
| HIGH 高 | 7 天 |
| EXTREME 极高 | 3 天 |

规则与实现位置：

- 周期常量：后端 `constants/RiskLevel.java`（`frequencyDays`），前端 `constants/RiskLevel.ts`（`RISK_FREQUENCY_DAYS`）。
- 接口：`POST /api/pipeline-segment/risk`，请求体 `{segmentId, riskLevel}`，响应 `{segment, points[]}`，其中 `points` 列出各启用点位的新到期日（`nextDueAt = lastCheckedAt + 周期`）。
- 业务约束：管段下任一巡检点存在待核实（PENDING）漏损报告时，风险不允许下调，后端返回 409 + `RISK_DOWNGRADE_BLOCKED`；前端保留原风险与原周期（store 仅在保存成功后更新，下拉框回退到原风险）。
- 停用（DISABLED）点位不参与周期重算；保存成功后页面列出各启用点位的新到期日。
- 后端不可达时，前端 `mocks/riskScheduleMock.ts` 在本地种子数据上复刻同一套规则，保证离线行为一致。
- 相关错误码：`RISK_DOWNGRADE_BLOCKED`、`SEGMENT_NOT_FOUND`（前后端 `errorCodes`/`errorMessages` 均有定义）。

## 为什么会牵一发动全身

实体字段、枚举、日志模板、错误消息、构造器、筛选器和展示组件被刻意拆散到多个目录；修改一个状态值通常需要同步类型、构造器、服务、控制器、store、页面、README 与数据库种子。

## License

MIT
