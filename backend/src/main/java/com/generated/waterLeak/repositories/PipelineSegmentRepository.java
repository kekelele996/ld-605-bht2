package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PipelineSegmentRepository {
  private final List<Map<String, Object>> rows = new ArrayList<>();

  public PipelineSegmentRepository() {
    rows.add(row(1, "SEG-001", "城东", "球墨铸铁", "DN300", "1998", "PZ-1", "LOW"));
    rows.add(row(2, "SEG-002", "城西", "钢管", "DN500", "2005", "PZ-2", "MEDIUM"));
    rows.add(row(3, "SEG-003", "江北", "PE", "DN200", "2012", "PZ-1", "HIGH"));
    rows.add(row(4, "SEG-004", "江南", "铸铁", "DN400", "1986", "PZ-3", "EXTREME"));
    rows.add(row(5, "SEG-005", "高新", "钢管", "DN600", "2016", "PZ-2", "MEDIUM"));
  }

  private static Map<String, Object> row(long id, String code, String district, String material,
      String diameter, String installYear, String pressureZone, String riskLevel) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("id", id);
    row.put("segment_code", code);
    row.put("district", district);
    row.put("material", material);
    row.put("diameter", diameter);
    row.put("install_year", installYear);
    row.put("pressure_zone", pressureZone);
    row.put("risk_level", riskLevel);
    return row;
  }

  public List<Map<String, Object>> findAll() {
    return rows;
  }

  public Optional<Map<String, Object>> findById(long id) {
    return rows.stream().filter(row -> ((Number) row.get("id")).longValue() == id).findFirst();
  }

  public void updateRiskLevel(long id, String riskLevel) {
    findById(id).ifPresent(row -> row.put("risk_level", riskLevel));
  }
}
