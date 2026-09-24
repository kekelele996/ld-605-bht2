export const RiskLevel = ["LOW","MEDIUM","HIGH","EXTREME"] as const;
export type RiskLevel = (typeof RiskLevel)[number];

/** 风险等级 -> 巡检周期（天）：低 30 / 中 14 / 高 7 / 极高 3，与后端 RiskLevel 保持一致。 */
export const RISK_CYCLE_DAYS: Record<RiskLevel, number> = {
  LOW: 30,
  MEDIUM: 14,
  HIGH: 7,
  EXTREME: 3
};

export const RiskLevelText: Record<RiskLevel, string> = {
  LOW: "低风险",
  MEDIUM: "中风险",
  HIGH: "高风险",
  EXTREME: "极高风险"
};
