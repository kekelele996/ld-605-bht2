package com.generated.waterLeak.models;

/** 巡检点：check_frequency 为巡检周期（天），next_due_at 为下一到期日（ISO-8601）。 */
public class InspectionPoint {
  public Long id;
  public Long pipeline_segment_id;
  public String point_code;
  public String point_type;
  public String address_desc;
  /** 巡检周期，单位天；与管段风险等级联动。 */
  public Integer check_frequency;
  /** 最近一次检查时间（ISO-8601），作为周期计算起点。 */
  public String last_checked_at;
  /** 下一巡检到期日（ISO-8601）= 最近检查时间 + 巡检周期。 */
  public String next_due_at;
  /** 点位状态：ENABLED 启用 / DISABLED 停用；仅启用点位参与到期日重排。 */
  public String status;

  public InspectionPoint() {}

  public InspectionPoint(Long id, Long pipeline_segment_id, String point_code, String point_type,
      String address_desc, Integer check_frequency, String last_checked_at,
      String next_due_at, String status) {
    this.id = id;
    this.pipeline_segment_id = pipeline_segment_id;
    this.point_code = point_code;
    this.point_type = point_type;
    this.address_desc = address_desc;
    this.check_frequency = check_frequency;
    this.last_checked_at = last_checked_at;
    this.next_due_at = next_due_at;
    this.status = status;
  }
}
