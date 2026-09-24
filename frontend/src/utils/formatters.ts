import { RiskLevelText } from "../constants/RiskLevel";

export const formatDate = (value: string) => new Date(value).toLocaleString("zh-CN");
/** 到期日按日期展示，避免时区时分干扰排期判断。 */
export const formatDateDay = (value: string) => {
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? value : date.toLocaleDateString("zh-CN");
};
export const formatStatus = (value: string) => value.replace(/_/g, " ");
export const formatNumber = (value: number) => new Intl.NumberFormat("zh-CN").format(value);
export const formatRisk = (value: string) =>
  (RiskLevelText as Record<string, string>)[value] ?? value;
/** 点位启停文案。 */
export const formatPointStatus = (value: string) =>
  value === "ENABLED" ? "启用" : value === "DISABLED" ? "停用" : value;
