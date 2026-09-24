package com.generated.waterLeak.models;

/** 管网分段：风险等级维护会联动下属巡检点的巡检周期。 */
public class PipelineSegment {
  public Long id;
  public String segment_code;
  public String district;
  public String material;
  public String diameter;
  public String install_year;
  public String pressure_zone;
  /** 风险等级：LOW / MEDIUM / HIGH / EXTREME。 */
  public String risk_level;

  public PipelineSegment() {}

  public PipelineSegment(Long id, String segment_code, String district, String material,
      String diameter, String install_year, String pressure_zone, String risk_level) {
    this.id = id;
    this.segment_code = segment_code;
    this.district = district;
    this.material = material;
    this.diameter = diameter;
    this.install_year = install_year;
    this.pressure_zone = pressure_zone;
    this.risk_level = risk_level;
  }
}
