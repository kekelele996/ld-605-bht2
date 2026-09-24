export const RiskLevel = ["LOW","MEDIUM","HIGH","EXTREME"] as const;
export type RiskLevel = (typeof RiskLevel)[number];
export const RiskLevelText: Record<RiskLevel, string> = Object.fromEntries(RiskLevel.map((value) => [value, value.replace(/_/g, " ")])) as Record<RiskLevel, string>;
// 风险保存后联动的巡检周期（天）：低30 / 中14 / 高7 / 极高3
export const RISK_FREQUENCY_DAYS: Record<RiskLevel, number> = { LOW: 30, MEDIUM: 14, HIGH: 7, EXTREME: 3 };
