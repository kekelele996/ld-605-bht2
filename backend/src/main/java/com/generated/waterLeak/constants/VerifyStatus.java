package com.generated.waterLeak.constants;

/** 漏损报告核实状态：PENDING_VERIFY 表示“待核实”，此类漏损存在时管段风险禁止下调。 */
public enum VerifyStatus {
  PENDING_VERIFY,
  VERIFIED,
  REJECTED;

  public static boolean isPending(String code) {
    return code != null && PENDING_VERIFY.name().equalsIgnoreCase(code.trim());
  }
}
