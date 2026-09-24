package com.generated.waterLeak.constructors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.types.InspectionScheduleItem;
import com.generated.waterLeak.types.RiskAdjustmentResult;

/** 管网分段响应对象构造器：controller/service 不得直接散写响应结构。 */
public final class PipelineSegmentDtoFactory {
  private PipelineSegmentDtoFactory() {}

  /** 兼容旧调用方的默认响应结构。 */
  public static Map<String, Object> create() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", 1);
    dto.put("segment_code", "管网分段");
    return dto;
  }

  public static Map<String, Object> toListItem(PipelineSegment segment, List<InspectionPoint> points) {
    return toListItemWithCount(segment, points.size());
  }

  public static Map<String, Object> toListItemWithCount(PipelineSegment segment, int enabledPointCount) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", segment.id);
    dto.put("segment_code", segment.segment_code);
    dto.put("district", segment.district);
    dto.put("material", segment.material);
    dto.put("diameter", segment.diameter);
    dto.put("install_year", segment.install_year);
    dto.put("pressure_zone", segment.pressure_zone);
    dto.put("risk_level", segment.risk_level);
    dto.put("enabled_point_count", enabledPointCount);
    return dto;
  }

  /** 风险保存结果：包含管段最新风险/周期与各启用点位新到期日。 */
  public static Map<String, Object> toRiskAdjustmentResponse(
      PipelineSegment segment, RiskAdjustmentResult result) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("segment", toListItemWithCount(segment, result.enabledPoints().size()));
    dto.put("segmentId", result.segmentId());
    dto.put("segmentCode", result.segmentCode());
    dto.put("previousRiskLevel", result.previousRiskLevel());
    dto.put("riskLevel", result.riskLevel());
    dto.put("cycleDays", result.cycleDays());
    dto.put("downgraded", result.downgraded());
    dto.put("pendingLeakExists", result.pendingLeakExists());
    dto.put("enabledPoints",
        result.enabledPoints().stream().map(PipelineSegmentDtoFactory::toScheduleItem).toList());
    return dto;
  }

  public static Map<String, Object> toScheduleItem(InspectionScheduleItem item) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("pointId", item.pointId());
    dto.put("pointCode", item.pointCode());
    dto.put("pipelineSegmentId", item.pipelineSegmentId());
    dto.put("cycleDays", item.cycleDays());
    dto.put("lastCheckedAt", item.lastCheckedAt());
    dto.put("nextDueAt", item.nextDueAt());
    dto.put("status", item.status());
    return dto;
  }
}
