import type { LeakReport } from "../types/LeakReport";

export const createDefaultLeakReport = (overrides: Partial<LeakReport> = {}): LeakReport => ({
  id: 1,
  reporter_type: "RESIDENT",
  point_id: 1,
  leak_level: "MINOR",
  description: "现场疑似渗水，待核实",
  reported_at: "2026-09-22T07:40:00Z",
  verify_status: "PENDING_VERIFY",
  photo_url: "/mock/leak-1.png",
  ...overrides
});

export const createLeakReportForm = createDefaultLeakReport;
export const createLeakReportResponse = createDefaultLeakReport;
