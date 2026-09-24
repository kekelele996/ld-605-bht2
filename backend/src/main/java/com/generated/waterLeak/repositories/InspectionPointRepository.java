package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InspectionPointRepository {
  private final List<Map<String, Object>> rows = new ArrayList<>();

  public InspectionPointRepository() {
    rows.add(row(1, 1, "PT-1001", "阀门井", "城东路 12 号", "30", "2026-09-10T08:00:00Z", "ENABLED"));
    rows.add(row(2, 2, "PT-1002", "流量计", "城西大道 88 号", "14", "2026-09-12T08:00:00Z", "ENABLED"));
    rows.add(row(3, 3, "PT-1003", "消火栓", "江北街 5 号", "7", "2026-09-15T08:00:00Z", "ENABLED"));
    rows.add(row(4, 3, "PT-1004", "排气阀", "江北街 9 号", "7", "2026-09-01T08:00:00Z", "DISABLED"));
    rows.add(row(5, 4, "PT-1005", "阀门井", "江南路 30 号", "3", "2026-09-20T08:00:00Z", "ENABLED"));
    rows.add(row(6, 5, "PT-1006", "流量计", "高新大道 101 号", "14", "2026-09-18T08:00:00Z", "ENABLED"));
    rows.add(row(7, 5, "PT-1007", "听漏点", "高新大道 150 号", "14", "2026-09-19T08:00:00Z", "ENABLED"));
  }

  private static Map<String, Object> row(long id, long segmentId, String pointCode, String pointType,
      String addressDesc, String checkFrequency, String lastCheckedAt, String status) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("id", id);
    row.put("pipeline_segment_id", segmentId);
    row.put("point_code", pointCode);
    row.put("point_type", pointType);
    row.put("address_desc", addressDesc);
    row.put("check_frequency", checkFrequency);
    row.put("last_checked_at", lastCheckedAt);
    row.put("status", status);
    return row;
  }

  public List<Map<String, Object>> findAll() {
    return rows;
  }

  public List<Map<String, Object>> findBySegmentId(long segmentId) {
    return rows.stream()
        .filter(row -> ((Number) row.get("pipeline_segment_id")).longValue() == segmentId)
        .toList();
  }

  public Optional<Map<String, Object>> findById(long id) {
    return rows.stream().filter(row -> ((Number) row.get("id")).longValue() == id).findFirst();
  }

  public void updateCheckFrequency(long id, String checkFrequency) {
    findById(id).ifPresent(row -> row.put("check_frequency", checkFrequency));
  }
}
