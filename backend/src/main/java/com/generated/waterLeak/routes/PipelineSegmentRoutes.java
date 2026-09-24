package com.generated.waterLeak.routes;

/** 管网分段路由：风险保存与巡检周期联动在同一路径下完成。 */
public final class PipelineSegmentRoutes {
  public static final String PATH = "/api/pipeline-segment";
  /** 保存风险并联动重排下属启用点位到期日。 */
  public static final String RISK_PATH = PATH + "/risk";
  /** 查询管段下点位（含停用）与到期日。 */
  public static final String POINTS_PATH = PATH + "/{id}/inspection-points";

  private PipelineSegmentRoutes() {}
}
