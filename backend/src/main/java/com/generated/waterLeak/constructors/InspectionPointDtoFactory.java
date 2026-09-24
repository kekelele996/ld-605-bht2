package com.generated.waterLeak.constructors;

import java.util.LinkedHashMap;
import java.util.Map;
import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.types.InspectionScheduleItem;

/** 巡检点响应对象构造器。 */
public final class InspectionPointDtoFactory {
  private InspectionPointDtoFactory() {}

  /** 兼容旧调用方的默认响应结构。 */
  public static Map<String, Object> create() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", 1);
    dto.put("point_code", "巡检点");
    return dto;
  }

  public static Map<String, Object> toListItem(InspectionPoint point) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", point.id);
    dto.put("pipeline_segment_id", point.pipeline_segment_id);
    dto.put("point_code", point.point_code);
    dto.put("point_type", point.point_type);
    dto.put("address_desc", point.address_desc);
    dto.put("check_frequency", point.check_frequency);
    dto.put("last_checked_at", point.last_checked_at);
    dto.put("next_due_at", point.next_due_at);
    dto.put("status", point.status);
    return dto;
  }

  public static InspectionScheduleItem toScheduleItem(InspectionPoint point) {
    return new InspectionScheduleItem(
        point.id,
        point.point_code,
        point.pipeline_segment_id,
        point.check_frequency,
        point.last_checked_at,
        point.next_due_at,
        point.status);
  }
}
