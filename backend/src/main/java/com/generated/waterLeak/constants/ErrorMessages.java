package com.generated.waterLeak.constants;

public final class ErrorMessages {
  public static final String AUTH_REQUIRED = "missing token";
  public static final String RBAC_DENIED = "role denied";
  public static final String VALIDATION_FAILED = "表单字段缺失或格式错误";
  public static final String RATE_LIMITED = "请求过于频繁，请稍后再试";

  public static final String SEGMENT_NOT_FOUND = "管网分段不存在，无法调整风险";
  public static final String INVALID_RISK_LEVEL = "风险等级必须为 LOW、MEDIUM、HIGH、EXTREME 之一";
  public static final String RISK_DOWNGRADE_BLOCKED =
      "该管段存在待核实漏损，风险等级不允许下调；请先完成漏损核实，原风险与原巡检周期已保留";
  public static final String MISSING_LAST_CHECKED_AT = "巡检点缺少最近一次检查时间，无法重排到期日";

  private ErrorMessages() {}
}
