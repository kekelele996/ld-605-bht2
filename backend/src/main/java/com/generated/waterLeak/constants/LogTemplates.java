package com.generated.waterLeak.constants;

/** 审计日志模板：所有写操作均需记录，字段变更时同步更新模板与调用处。 */
public final class LogTemplates {
  public static final String CREATE = "create";
  public static final String UPDATE = "update";
  public static final String STATUS = "status";
  public static final String EXPORT = "export";

  /** 管段风险等级调整：记录原风险 -> 新风险。 */
  public static final String PIPELINE_RISK_CHANGED =
      "管段[%s]风险等级由 %s 调整为 %s，巡检周期联动为 %d 天";
  /** 管段风险调整被拒（存在待核实漏损），原风险与原周期保留。 */
  public static final String PIPELINE_RISK_DOWNGRADE_REJECTED =
      "管段[%s]风险下调被拒：存在待核实漏损，保留原风险 %s 与原巡检周期 %d 天";
  /** 巡检周期重排：记录点位与新到期日。 */
  public static final String INSPECTION_DUE_DATE_RESCHEDULED =
      "巡检点[%s]按风险 %s（周期 %d 天）重排到期日，起点取最近检查时间 %s，新到期日 %s";

  private LogTemplates() {}
}
