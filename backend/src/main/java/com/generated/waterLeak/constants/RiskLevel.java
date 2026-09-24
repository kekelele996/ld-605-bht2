package com.generated.waterLeak.constants;

/** 管段风险等级。rank 越大风险越高；frequencyDays 为风险联动的巡检周期（天）。 */
public enum RiskLevel {
  LOW(30),
  MEDIUM(14),
  HIGH(7),
  EXTREME(3);

  private final int frequencyDays;

  RiskLevel(int frequencyDays) {
    this.frequencyDays = frequencyDays;
  }

  /** 风险保存后巡检点采用的巡检周期（天）。 */
  public int frequencyDays() {
    return frequencyDays;
  }

  /** 风险高低次序，用于判断本次调整是否属于下调。 */
  public int rank() {
    return ordinal();
  }
}
