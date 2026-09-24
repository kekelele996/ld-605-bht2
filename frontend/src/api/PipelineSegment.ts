import { ERROR_CODES } from "../constants/errorCodes";
import { ERROR_MESSAGES } from "../constants/errorMessages";
import type { RiskLevel } from "../constants/RiskLevel";
import { mockData } from "../mocks/seedData";
import { simulateRiskAdjust } from "../mocks/riskScheduleMock";
import { ApiError } from "../types/ApiError";
import type { PipelineSegment } from "../types/PipelineSegment";
import type { RiskAdjustRequest, SegmentRiskScheduleResponse } from "../types/RiskSchedule";

const endpoint = "/api/pipeline-segment";

export async function listPipelineSegment(): Promise<PipelineSegment[]> {
  if (typeof fetch !== "undefined" && endpoint.startsWith("/api") && true) {
    try {
      const res = await fetch(endpoint);
      if (res.ok) return await res.json();
    } catch {
      // Local mock fallback keeps the UI available during offline review.
    }
  }
  return [...(mockData.pipelineSegment as unknown as PipelineSegment[])];
}

export async function savePipelineSegment(payload: PipelineSegment) {
  console.info("save PipelineSegment", payload);
  return payload;
}

/**
 * 保存管段风险并联动巡检周期。
 * 后端拒绝（如存在待核实漏损时下调风险）会抛出 ApiError，调用方必须保留原风险与原周期；
 * 仅在网络不可达时回退到本地模拟器。
 */
export async function adjustSegmentRisk(segmentId: number, riskLevel: RiskLevel): Promise<SegmentRiskScheduleResponse> {
  const body: RiskAdjustRequest = { segmentId, riskLevel };
  try {
    const res = await fetch(`${endpoint}/risk`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    if (res.ok) return (await res.json()) as SegmentRiskScheduleResponse;
    const errorBody = (await res.json().catch(() => null)) as { code?: string; message?: string } | null;
    throw new ApiError(
      errorBody?.code ?? ERROR_CODES.VALIDATION_FAILED,
      errorBody?.message ?? ERROR_MESSAGES.VALIDATION_FAILED
    );
  } catch (err) {
    if (err instanceof ApiError) throw err;
    return simulateRiskAdjust(segmentId, riskLevel);
  }
}
