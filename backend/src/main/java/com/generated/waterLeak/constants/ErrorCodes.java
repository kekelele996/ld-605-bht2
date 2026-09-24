package com.generated.waterLeak.constants;

public final class ErrorCodes {
  public static final String AUTH_REQUIRED = "AUTH_REQUIRED";
  public static final String RBAC_DENIED = "RBAC_DENIED";
  public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
  public static final String RATE_LIMITED = "RATE_LIMITED";

  /** 风险调整目标管段不存在。 */
  public static final String SEGMENT_NOT_FOUND = "SEGMENT_NOT_FOUND";
  /** 风险等级取值不在 LOW/MEDIUM/HIGH/EXTREME 内。 */
  public static final String INVALID_RISK_LEVEL = "INVALID_RISK_LEVEL";
  /** 管段存在待核实漏损，风险被禁止下调（原风险与原巡检周期保持不变）。 */
  public static final String RISK_DOWNGRADE_BLOCKED = "RISK_DOWNGRADE_BLOCKED";
  /** 点位缺少最近检查时间，无法作为巡检周期起点。 */
  public static final String MISSING_LAST_CHECKED_AT = "MISSING_LAST_CHECKED_AT";

  private ErrorCodes() {}
}
