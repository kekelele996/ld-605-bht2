package com.generated.waterLeak.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.generated.waterLeak.constructors.InspectionPointDtoFactory;
import com.generated.waterLeak.constructors.PipelineSegmentDtoFactory;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.RiskLevel;
import com.generated.waterLeak.middlewares.AuditLogMiddleware;
import com.generated.waterLeak.models.InspectionPoint;
import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.repositories.PipelineSegmentRepository;
import com.generated.waterLeak.types.InspectionScheduleItem;
import com.generated.waterLeak.types.PipelineSegmentPayload;
import com.generated.waterLeak.types.RiskAdjustmentException;
import com.generated.waterLeak.types.RiskAdjustmentResult;
import com.generated.waterLeak.utils.InspectionScheduleCalculator;

/**
 * 管网分段服务：风险等级保存与巡检周期联动。
 *
 * <p>联动规则：
 * <ul>
 *   <li>低风险 30 天、中风险 14 天、高风险 7 天、极高风险 3 天；</li>
 *   <li>新到期日起点取该点位最近一次检查时间；</li>
 *   <li>管段存在待核实漏损时，风险不允许下降，原风险与原周期全部保留；</li>
 *   <li>任何一步拒绝，都必须发生在落库之前。</li>
 * </ul>
 */
@Service
public class PipelineSegmentService {
  private final PipelineSegmentRepository segmentRepo;
  private final InspectionPointRepository pointRepo;
  private final LeakReportRepository leakRepo;
  private final AuditLogMiddleware auditLog;

  public PipelineSegmentService(PipelineSegmentRepository segmentRepo,
      InspectionPointRepository pointRepo, LeakReportRepository leakRepo,
      AuditLogMiddleware auditLog) {
    this.segmentRepo = segmentRepo;
    this.pointRepo = pointRepo;
    this.leakRepo = leakRepo;
    this.auditLog = auditLog;
  }

  /** 管段列表：附带各管段启用点位数。 */
  public List<Map<String, Object>> list() {
    List<Map<String, Object>> rows = new ArrayList<>();
    for (PipelineSegment segment : segmentRepo.findAll()) {
      rows.add(PipelineSegmentDtoFactory.toListItem(
          segment, pointRepo.findEnabledBySegmentId(segment.id)));
    }
    return rows;
  }

  /**
   * 保存管段风险并联动巡检周期。
   *
   * @return 保存结果，含各启用点位的新到期日清单
   * @throws RiskAdjustmentException 管段不存在 / 风险值非法 / 待核实漏损阻止下调；
   *         抛出前不写任何数据，原风险与原周期保留
   */
  public RiskAdjustmentResult saveRiskLevel(PipelineSegmentPayload payload) {
    // ---- 1. 入参与管段校验（拒绝时仓储零写入）----
    if (payload == null || payload.id() == null) {
      throw RiskAdjustmentException.invalidRiskLevel();
    }
    PipelineSegment segment = segmentRepo.findById(payload.id())
        .orElseThrow(RiskAdjustmentException::segmentNotFound);
    RiskLevel targetRisk = RiskLevel.fromCode(payload.risk_level())
        .orElseThrow(RiskAdjustmentException::invalidRiskLevel);
    RiskLevel previousRisk = RiskLevel.fromCode(segment.risk_level)
        .orElseThrow(RiskAdjustmentException::invalidRiskLevel);

    List<InspectionPoint> enabledPoints =
        pointRepo.findEnabledBySegmentId(segment.id);
    List<Long> segmentPointIds = pointRepo.findBySegmentId(segment.id).stream()
        .map(point -> point.id).toList();
    boolean pendingLeakExists =
        leakRepo.existsPendingVerifyBySegmentId(segment.id, segmentPointIds);

    // target 风险低于 previous 即为“风险下降”（如 HIGH -> MEDIUM）
    boolean downgraded = targetRisk.isLowerThan(previousRisk);
    if (downgraded && pendingLeakExists) {
      // 待核实漏损未关闭：拒绝风险下降，保留原风险与原巡检周期。
      auditLog.record(String.format(LogTemplates.PIPELINE_RISK_DOWNGRADE_REJECTED,
          segment.segment_code, previousRisk.name(), previousRisk.getCycleDays()));
      throw RiskAdjustmentException.downgradeBlocked();
    }

    // ---- 2. 周期与新到期日预计算（仍不落库；计算失败同样整体拒绝）----
    int cycleDays = targetRisk.getCycleDays();
    List<InspectionScheduleItem> scheduleItems = new ArrayList<>();
    for (InspectionPoint point : enabledPoints) {
      String nextDueAt;
      try {
        nextDueAt = InspectionScheduleCalculator.calculateNextDueAt(
            point.last_checked_at, cycleDays);
      } catch (IllegalArgumentException ex) {
        auditLog.record(String.format(
            LogTemplates.INSPECTION_DUE_DATE_RESCHEDULED,
            point.point_code, targetRisk.name(), cycleDays,
            String.valueOf(point.last_checked_at), "CALCULATION_FAILED"));
        throw RiskAdjustmentException.missingLastCheckedAt(point.id);
      }
      scheduleItems.add(new InspectionScheduleItem(
          point.id, point.point_code, point.pipeline_segment_id,
          cycleDays, point.last_checked_at, nextDueAt, point.status));
    }

    // ---- 3. 全部校验/预计算通过后才落库：风险与周期一起提交 ----
    segmentRepo.updateRiskLevel(segment.id, targetRisk.name());
    pointRepo.rescheduleEnabledPointsBySegment(segment.id, cycleDays,
        point -> InspectionScheduleCalculator.calculateNextDueAt(
            point.last_checked_at, cycleDays));

    auditLog.record(String.format(LogTemplates.PIPELINE_RISK_CHANGED,
        segment.segment_code, previousRisk.name(), targetRisk.name(), cycleDays));
    for (InspectionScheduleItem item : scheduleItems) {
      auditLog.record(String.format(LogTemplates.INSPECTION_DUE_DATE_RESCHEDULED,
          item.pointCode(), targetRisk.name(), cycleDays,
          item.lastCheckedAt(), item.nextDueAt()));
    }

    return new RiskAdjustmentResult(
        segment.id,
        segment.segment_code,
        previousRisk.name(),
        targetRisk.name(),
        cycleDays,
        downgraded,
        pendingLeakExists,
        scheduleItems);
  }

  /** 供控制器在保存后回读最新管段快照（含启用点位数）。 */
  public PipelineSegment getSegment(Long id) {
    return segmentRepo.findById(id)
        .orElseThrow(RiskAdjustmentException::segmentNotFound);
  }

  /** 列出某管段下所有点位（含停用），控制器不直接访问仓储。 */
  public List<Map<String, Object>> listPointsOfSegment(Long segmentId) {
    List<Map<String, Object>> rows = new ArrayList<>();
    for (InspectionPoint point : pointRepo.findBySegmentId(segmentId)) {
      rows.add(InspectionPointDtoFactory.toListItem(point));
    }
    return rows;
  }
}
