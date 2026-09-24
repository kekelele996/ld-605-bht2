export const PointStatus = ["ENABLED","DISABLED"] as const;
export type PointStatus = (typeof PointStatus)[number];
export const PointStatusText: Record<PointStatus, string> = { ENABLED: "启用", DISABLED: "停用" };
