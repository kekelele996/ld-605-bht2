package com.generated.waterLeak.types;

/** 管段风险调整请求体：segmentId + 目标风险等级。 */
public record RiskAdjustRequest(Long segmentId, String riskLevel) {}
