export const VerifyStatus = ["PENDING","CONFIRMED","DISMISSED"] as const;
export type VerifyStatus = (typeof VerifyStatus)[number];
export const VerifyStatusText: Record<VerifyStatus, string> = { PENDING: "待核实", CONFIRMED: "已确认", DISMISSED: "已排除" };
