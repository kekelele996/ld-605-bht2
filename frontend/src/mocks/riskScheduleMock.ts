import { ERROR_CODES } from "../constants/errorCodes";
import { ERROR_MESSAGES } from "../constants/errorMessages";
import type { RiskLevel } from "../constants/RiskLevel";
import { ApiError } from "../types/ApiError";
import type { PipelineSegment } from "../types/PipelineSegment";
import type { SegmentRiskScheduleResponse } from "../types/RiskSchedule";
import { computeNextDueAt, frequencyDaysForRisk, isRiskDowngrade } from "../utils/inspectionSchedule";
import { mockData } from "./seedData";

/**
 * 离线兜底模拟器：后端不可达时在本地种子数据上复刻同一套风险联动规则，
 * 保证离线评审时“待核实漏损禁止下调 / 周期联动重算”的行为一致。
 */
export function simulateRiskAdjust(segmentId: number, riskLevel: RiskLevel): SegmentRiskScheduleResponse {
  const segment = mockData.pipelineSegment.find((row) => row.id === segmentId);
  if (!segment) {
    throw new ApiError(ERROR_CODES.SEGMENT_NOT_FOUND, ERROR_MESSAGES.SEGMENT_NOT_FOUND);
  }
  const points = mockData.inspectionPoint.filter((row) => row.pipeline_segment_id === segmentId);
  if (isRiskDowngrade(segment.risk_level as RiskLevel, riskLevel)) {
    const hasPending = mockData.leakReport.some(
      (report) => report.verify_status === "PENDING" && points.some((point) => point.id === report.point_id)
    );
    if (hasPending) {
      throw new ApiError(ERROR_CODES.RISK_DOWNGRADE_BLOCKED, ERROR_MESSAGES.RISK_DOWNGRADE_BLOCKED);
    }
  }
  const frequencyDays = frequencyDaysForRisk(riskLevel);
  return {
    segment: { ...segment, risk_level: riskLevel } as unknown as PipelineSegment,
    points: points
      .filter((point) => point.status === "ENABLED")
      .map((point) => ({
        pointId: point.id,
        pointCode: point.point_code,
        frequencyDays,
        lastCheckedAt: point.last_checked_at,
        nextDueAt: computeNextDueAt(point.last_checked_at, frequencyDays)
      }))
  };
}
