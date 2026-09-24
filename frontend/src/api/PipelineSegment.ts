import { mockData } from "../mocks/seedData";
import { ERROR_MESSAGES } from "../constants/errorMessages";
import type { ApiError } from "../types/ApiError";
import type {
  PipelineSegment,
  RiskAdjustmentResult,
  RiskSavePayload
} from "../types/PipelineSegment";

const endpoint = "/api/pipeline-segment";

/** 携带后端错误码的异常：store/页面据此决定保留原风险与原周期。 */
export class ApiRequestError extends Error implements ApiError {
  code: string;
  constructor(code: string, message: string) {
    super(message);
    this.name = "ApiRequestError";
    this.code = code;
  }
}

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

/**
 * 保存管段风险并联动巡检周期。
 * 成功：返回新风险、新周期及各启用点位的新到期日。
 * 失败（如待核实漏损阻止下调）：抛出 ApiRequestError，调用方必须保留原风险与原周期。
 */
export async function savePipelineSegmentRisk(
  payload: RiskSavePayload
): Promise<RiskAdjustmentResult> {
  const res = await fetch(`${endpoint}/risk`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (res.ok) {
    return (await res.json()) as RiskAdjustmentResult;
  }
  let body: Partial<ApiError> = {};
  try {
    body = await res.json();
  } catch {
    // 后端不可达或返回非 JSON 错误页时使用兜底文案。
  }
  throw new ApiRequestError(
    body.code ?? "NETWORK_ERROR",
    body.message ?? ERROR_MESSAGES.VALIDATION_FAILED
  );
}
