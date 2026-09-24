package com.generated.waterLeak.models;

/** 漏损报告：verify_status=PENDING_VERIFY 表示待核实，此时所属管段禁止风险下调。 */
public class LeakReport {
  public Long id;
  public String reporter_type;
  public Long point_id;
  public String leak_level;
  public String description;
  public String reported_at;
  /** 核实状态：PENDING_VERIFY / VERIFIED / REJECTED。 */
  public String verify_status;
  public String photo_url;

  public LeakReport() {}

  public LeakReport(Long id, String reporter_type, Long point_id, String leak_level,
      String description, String reported_at, String verify_status, String photo_url) {
    this.id = id;
    this.reporter_type = reporter_type;
    this.point_id = point_id;
    this.leak_level = leak_level;
    this.description = description;
    this.reported_at = reported_at;
    this.verify_status = verify_status;
    this.photo_url = photo_url;
  }
}
