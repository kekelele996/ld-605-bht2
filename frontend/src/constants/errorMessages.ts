export const ERROR_MESSAGES = {
  AUTH_REQUIRED: "请先登录后再继续操作",
  RBAC_DENIED: "当前角色没有执行该动作的权限",
  VALIDATION_FAILED: "表单字段缺失或格式错误",
  RATE_LIMITED: "请求过于频繁，请稍后再试",
  SEGMENT_NOT_FOUND: "管网分段不存在，无法调整风险",
  INVALID_RISK_LEVEL: "风险等级必须为低、中、高、极高之一",
  RISK_DOWNGRADE_BLOCKED: "该管段存在待核实漏损，风险不允许下调，已保留原风险与原巡检周期",
  MISSING_LAST_CHECKED_AT: "巡检点缺少最近一次检查时间，无法重排到期日"
};
