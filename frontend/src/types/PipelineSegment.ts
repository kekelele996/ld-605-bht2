export interface PipelineSegment {
  id: number;
  segment_code: string;
  district: string;
  material: string;
  diameter: string;
  install_year: string;
  pressure_zone: string;
  risk_level: string;
  /** 列表接口附带：该管段下启用点位数。 */
  enabled_point_count?: number;
}

/** 风险保存请求：与后端 PipelineSegmentPayload 对齐。 */
export interface RiskSavePayload {
  id: number;
  risk_level: string;
}

/** 单个启用点位联动后的新到期日。 */
export interface InspectionScheduleItem {
  pointId: number;
  pointCode: string;
  pipelineSegmentId: number;
  cycleDays: number;
  lastCheckedAt: string;
  nextDueAt: string;
  status: string;
}

/** PUT /api/pipeline-segment/risk 响应。 */
export interface RiskAdjustmentResult {
  segment: PipelineSegment;
  segmentId: number;
  segmentCode: string;
  previousRiskLevel: string;
  riskLevel: string;
  cycleDays: number;
  downgraded: boolean;
  pendingLeakExists: boolean;
  enabledPoints: InspectionScheduleItem[];
}
