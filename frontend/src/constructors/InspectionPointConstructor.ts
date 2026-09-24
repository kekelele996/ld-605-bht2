import type { InspectionPoint } from "../types/InspectionPoint";

export const createDefaultInspectionPoint = (overrides: Partial<InspectionPoint> = {}): InspectionPoint => ({
  id: 1,
  pipeline_segment_id: 1,
  point_code: "PT-001-01",
  point_type: "闸阀井",
  address_desc: "江岸区沿江大道 12 号",
  check_frequency: 30,
  last_checked_at: "2026-09-01T09:00:00Z",
  next_due_at: "2026-10-01T00:00:00Z",
  status: "ENABLED",
  ...overrides
});

export const createInspectionPointForm = createDefaultInspectionPoint;
export const createInspectionPointResponse = createDefaultInspectionPoint;
