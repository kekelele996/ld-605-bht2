export interface InspectionPoint {
  id: number;
  pipeline_segment_id: number;
  point_code: string;
  point_type: string;
  address_desc: string;
  /** 巡检周期（天），随管段风险联动：低 30 / 中 14 / 高 7 / 极高 3。 */
  check_frequency: number;
  /** 最近一次检查时间，作为周期起点。 */
  last_checked_at: string;
  /** 下一巡检到期日 = 最近检查时间 + 周期。 */
  next_due_at?: string;
  /** ENABLED 启用 / DISABLED 停用；仅启用点位参与联动与到期日清单。 */
  status: string;
}
