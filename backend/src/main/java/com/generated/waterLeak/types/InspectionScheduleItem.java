package com.generated.waterLeak.types;

/**
 * 单个启用点位的巡检周期重排结果。
 * 起点 lastCheckedAt（最近一次检查时间），到期日 nextDueAt = 起点 + cycleDays。
 */
public record InspectionScheduleItem(
    Long pointId,
    String pointCode,
    Long pipelineSegmentId,
    Integer cycleDays,
    String lastCheckedAt,
    String nextDueAt,
    String status) {}
