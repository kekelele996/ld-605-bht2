package com.generated.waterLeak.types;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;

/**
 * 风险调整/巡检周期联动业务异常。
 * 抛出时必须保证尚未写入任何风险与周期变更，即原风险、原周期保持不变。
 */
public class RiskAdjustmentException extends RuntimeException {
  private final String code;

  public RiskAdjustmentException(String code, String message) {
    super(message);
    this.code = code;
  }

  public static RiskAdjustmentException segmentNotFound() {
    return new RiskAdjustmentException(
        ErrorCodes.SEGMENT_NOT_FOUND, ErrorMessages.SEGMENT_NOT_FOUND);
  }

  public static RiskAdjustmentException invalidRiskLevel() {
    return new RiskAdjustmentException(
        ErrorCodes.INVALID_RISK_LEVEL, ErrorMessages.INVALID_RISK_LEVEL);
  }

  public static RiskAdjustmentException downgradeBlocked() {
    return new RiskAdjustmentException(
        ErrorCodes.RISK_DOWNGRADE_BLOCKED, ErrorMessages.RISK_DOWNGRADE_BLOCKED);
  }

  public static RiskAdjustmentException missingLastCheckedAt(Long pointId) {
    return new RiskAdjustmentException(
        ErrorCodes.MISSING_LAST_CHECKED_AT,
        ErrorMessages.MISSING_LAST_CHECKED_AT + "（pointId=" + pointId + "）");
  }

  public String getCode() {
    return code;
  }
}
