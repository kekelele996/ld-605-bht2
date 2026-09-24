import { RISK_FREQUENCY_DAYS, RiskLevel } from "../constants/RiskLevel";

const DAY_MS = 24 * 60 * 60 * 1000;

/** 风险等级对应的巡检周期（天）。 */
export const frequencyDaysForRisk = (risk: RiskLevel): number => RISK_FREQUENCY_DAYS[risk];

/** 判断风险调整是否属于下调（RiskLevel 数组按风险升序排列）。 */
export const isRiskDowngrade = (from: RiskLevel, to: RiskLevel): boolean =>
  RiskLevel.indexOf(to) < RiskLevel.indexOf(from);

/** 新到期日 = 最近一次检查时间 + 巡检周期；格式与后端 Instant.toString() 对齐（不带毫秒）。 */
export const computeNextDueAt = (lastCheckedAt: string, frequencyDays: number): string =>
  new Date(Date.parse(lastCheckedAt) + frequencyDays * DAY_MS).toISOString().replace(/\.\d{3}Z$/, "Z");
