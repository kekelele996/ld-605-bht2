import type { PipelineSegment, RiskSavePayload } from "../types/PipelineSegment";

export const createDefaultPipelineSegment = (overrides: Partial<PipelineSegment> = {}): PipelineSegment => ({
  id: 1,
  segment_code: "PN-2026-001",
  district: "江岸区",
  material: "球墨铸铁",
  diameter: "DN300",
  install_year: "2014",
  pressure_zone: "中压区",
  risk_level: "LOW",
  enabled_point_count: 0,
  ...overrides
});

/** 管网页“风险调整”表单：默认风险随选中管段传入。 */
export const createRiskSavePayload = (
  overrides: Partial<RiskSavePayload> = {}
): RiskSavePayload => ({
  id: 1,
  risk_level: "LOW",
  ...overrides
});

export const createPipelineSegmentForm = createDefaultPipelineSegment;
export const createPipelineSegmentResponse = createDefaultPipelineSegment;
