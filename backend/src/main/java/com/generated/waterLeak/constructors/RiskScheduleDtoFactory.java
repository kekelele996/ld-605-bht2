package com.generated.waterLeak.constructors;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 风险调整响应构造器：组装管段结果与各启用巡检点的新到期日。 */
public final class RiskScheduleDtoFactory {

  private RiskScheduleDtoFactory() {}

  /** 单个启用点位的周期重算结果：起点取最近一次检查时间。 */
  public static Map<String, Object> pointEntry(Map<String, Object> point, int frequencyDays) {
    String lastCheckedAt = String.valueOf(point.get("last_checked_at"));
    String nextDueAt = Instant.parse(lastCheckedAt).plus(Duration.ofDays(frequencyDays)).toString();
    Map<String, Object> entry = new LinkedHashMap<>();
    entry.put("pointId", ((Number) point.get("id")).longValue());
    entry.put("pointCode", point.get("point_code"));
    entry.put("frequencyDays", frequencyDays);
    entry.put("lastCheckedAt", lastCheckedAt);
    entry.put("nextDueAt", nextDueAt);
    return entry;
  }

  /** 管网页保存成功后的响应：更新后的管段 + 各启用点位的新到期日列表。 */
  public static Map<String, Object> response(Map<String, Object> segment, List<Map<String, Object>> points) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("segment", segment);
    body.put("points", points);
    return body;
  }
}
