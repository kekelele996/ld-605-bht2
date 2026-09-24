package com.generated.waterLeak.types;

import java.util.List;

/**
 * 风险保存结果：保存成功后返回管段最新风险/周期，以及该管段下各启用点位的新到期日。
 */
public record RiskAdjustmentResult(
    Long segmentId,
    String segmentCode,
    String previousRiskLevel,
    String riskLevel,
    Integer cycleDays,
    boolean downgraded,
    boolean pendingLeakExists,
    List<InspectionScheduleItem> enabledPoints) {}
