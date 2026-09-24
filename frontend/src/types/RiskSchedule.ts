import type { PipelineSegment } from "./PipelineSegment";

/** 风险调整请求体，对应后端 RiskAdjustRequest。 */
export interface RiskAdjustRequest {
  segmentId: number;
  riskLevel: string;
}

/** 单个启用巡检点的周期重算结果。 */
export interface PointScheduleEntry {
  pointId: number;
  pointCode: string;
  frequencyDays: number;
  lastCheckedAt: string;
  nextDueAt: string;
}

/** 风险保存成功后的响应：更新后的管段 + 各启用点位的新到期日。 */
export interface SegmentRiskScheduleResponse {
  segment: PipelineSegment;
  points: PointScheduleEntry[];
}
