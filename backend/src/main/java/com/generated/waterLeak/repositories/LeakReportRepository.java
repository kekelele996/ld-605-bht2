package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class LeakReportRepository {
  private final List<Map<String, Object>> rows = new ArrayList<>();

  public LeakReportRepository() {
    rows.add(row(1, "RESIDENT", 2, "MINOR", "城西大道井盖渗水", "2026-09-13T10:00:00Z", "PENDING", "/mock/leak-1.png"));
    rows.add(row(2, "INSPECTOR", 3, "MAJOR", "江北街管段接口渗漏", "2026-09-16T09:30:00Z", "CONFIRMED", "/mock/leak-2.png"));
    rows.add(row(3, "RESIDENT", 6, "BURST", "高新大道疑似爆管", "2026-09-21T07:45:00Z", "PENDING", "/mock/leak-3.png"));
  }

  private static Map<String, Object> row(long id, String reporterType, long pointId, String leakLevel,
      String description, String reportedAt, String verifyStatus, String photoUrl) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("id", id);
    row.put("reporter_type", reporterType);
    row.put("point_id", pointId);
    row.put("leak_level", leakLevel);
    row.put("description", description);
    row.put("reported_at", reportedAt);
    row.put("verify_status", verifyStatus);
    row.put("photo_url", photoUrl);
    return row;
  }

  public List<Map<String, Object>> findAll() {
    return rows;
  }

  public List<Map<String, Object>> findByPointIds(Collection<Long> pointIds) {
    return rows.stream()
        .filter(row -> pointIds.contains(((Number) row.get("point_id")).longValue()))
        .toList();
  }
}
