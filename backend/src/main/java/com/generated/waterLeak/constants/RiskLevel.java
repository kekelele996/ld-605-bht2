package com.generated.waterLeak.constants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** 管段风险等级：风险调整与巡检周期联动，枚举顺序即风险由低到高。 */
public enum RiskLevel {
  LOW,
  MEDIUM,
  HIGH,
  EXTREME;

  /** 风险等级 -> 巡检周期（天）：低风险 30 天、中风险 14 天、高风险 7 天、极高风险 3 天。 */
  private static final Map<RiskLevel, Integer> INSPECTION_CYCLE_DAYS = new LinkedHashMap<>();

  static {
    INSPECTION_CYCLE_DAYS.put(LOW, 30);
    INSPECTION_CYCLE_DAYS.put(MEDIUM, 14);
    INSPECTION_CYCLE_DAYS.put(HIGH, 7);
    INSPECTION_CYCLE_DAYS.put(EXTREME, 3);
  }

  public int getCycleDays() {
    return INSPECTION_CYCLE_DAYS.get(this);
  }

  public static Optional<RiskLevel> fromCode(String code) {
    if (code == null) {
      return Optional.empty();
    }
    String normalized = code.trim().toUpperCase();
    for (RiskLevel level : values()) {
      if (level.name().equals(normalized)) {
        return Optional.of(level);
      }
    }
    return Optional.empty();
  }

  /** 当前风险是否低于 other（如 MEDIUM.isLowerThan(HIGH) == true）。 */
  public boolean isLowerThan(RiskLevel other) {
    return this.ordinal() < other.ordinal();
  }
}
