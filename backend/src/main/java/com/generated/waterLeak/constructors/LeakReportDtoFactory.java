package com.generated.waterLeak.constructors;

import java.util.LinkedHashMap;
import java.util.Map;
import com.generated.waterLeak.models.LeakReport;

/** 漏损报告响应对象构造器。 */
public final class LeakReportDtoFactory {
  private LeakReportDtoFactory() {}

  public static Map<String, Object> create() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", 1);
    dto.put("leak_level", "漏损报告");
    return dto;
  }

  public static Map<String, Object> toListItem(LeakReport report) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", report.id);
    dto.put("reporter_type", report.reporter_type);
    dto.put("point_id", report.point_id);
    dto.put("leak_level", report.leak_level);
    dto.put("description", report.description);
    dto.put("reported_at", report.reported_at);
    dto.put("verify_status", report.verify_status);
    dto.put("photo_url", report.photo_url);
    return dto;
  }
}
