package com.generated.waterLeak.constants;

/** 巡检点启停状态：风险联动重排巡检周期时，仅 ENABLED 点位参与到期日计算。 */
public enum PointStatus {
  ENABLED,
  DISABLED;

  public static boolean isEnabled(String code) {
    return code != null && ENABLED.name().equalsIgnoreCase(code.trim());
  }
}
