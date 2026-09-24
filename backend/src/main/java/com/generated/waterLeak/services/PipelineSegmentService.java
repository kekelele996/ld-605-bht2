package com.generated.waterLeak.services;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.constants.LogTemplates;
import com.generated.waterLeak.constants.PointStatus;
import com.generated.waterLeak.constants.RiskLevel;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.constructors.RiskScheduleDtoFactory;
import com.generated.waterLeak.middlewares.AuditLogMiddleware;
import com.generated.waterLeak.repositories.InspectionPointRepository;
import com.generated.waterLeak.repositories.LeakReportRepository;
import com.generated.waterLeak.repositories.PipelineSegmentRepository;
import com.generated.waterLeak.types.RiskAdjustRequest;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PipelineSegmentService {
  private final PipelineSegmentRepository repo;
  private final InspectionPointRepository inspectionPointRepo;
  private final LeakReportRepository leakReportRepo;

  public PipelineSegmentService(PipelineSegmentRepository repo,
      InspectionPointRepository inspectionPointRepo, LeakReportRepository leakReportRepo) {
    this.repo = repo;
    this.inspectionPointRepo = inspectionPointRepo;
    this.leakReportRepo = leakReportRepo;
  }

  public List<Map<String, Object>> list() {
    return repo.findAll();
  }

  /**
   * 班组长调整管段风险：保存成功后按风险联动巡检周期（低30/中14/高7/极高3 天），
   * 起点取各启用点位最近一次检查时间；存在待核实漏损报告时拒绝风险下调。
   */
  public Map<String, Object> adjustRisk(RiskAdjustRequest request) {
    if (request == null || request.segmentId() == null || request.riskLevel() == null) {
      throw new RiskAdjustmentException(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    final RiskLevel newRisk;
    try {
      newRisk = RiskLevel.valueOf(request.riskLevel());
    } catch (IllegalArgumentException ex) {
      throw new RiskAdjustmentException(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
    long segmentId = request.segmentId();
    Map<String, Object> segment = repo.findById(segmentId)
        .orElseThrow(() -> new RiskAdjustmentException(ErrorCodes.SEGMENT_NOT_FOUND, ErrorMessages.SEGMENT_NOT_FOUND));
    RiskLevel oldRisk = RiskLevel.valueOf(String.valueOf(segment.get("risk_level")));

    List<Map<String, Object>> points = inspectionPointRepo.findBySegmentId(segmentId);
    if (newRisk.rank() < oldRisk.rank() && hasPendingLeakReport(points)) {
      AuditLogMiddleware.record(LogTemplates.RISK_ADJUST_REJECTED,
          "segment=" + segmentId + " " + oldRisk + "->" + newRisk);
      throw new RiskAdjustmentException(ErrorCodes.RISK_DOWNGRADE_BLOCKED, ErrorMessages.RISK_DOWNGRADE_BLOCKED);
    }

    repo.updateRiskLevel(segmentId, newRisk.name());
    int frequencyDays = newRisk.frequencyDays();
    List<Map<String, Object>> entries = points.stream()
        .filter(point -> PointStatus.ENABLED.name().equals(point.get("status")))
        .map(point -> {
          long pointId = ((Number) point.get("id")).longValue();
          inspectionPointRepo.updateCheckFrequency(pointId, String.valueOf(frequencyDays));
          return RiskScheduleDtoFactory.pointEntry(point, frequencyDays);
        })
        .toList();

    AuditLogMiddleware.record(LogTemplates.RISK_ADJUST,
        "segment=" + segmentId + " " + oldRisk + "->" + newRisk + " frequencyDays=" + frequencyDays);
    AuditLogMiddleware.record(LogTemplates.SCHEDULE_RECOMPUTED,
        "segment=" + segmentId + " enabledPoints=" + entries.size());
    return RiskScheduleDtoFactory.response(repo.findById(segmentId).orElseThrow(), entries);
  }

  /** 管段下任一巡检点存在待核实漏损报告时，风险不允许下降。 */
  private boolean hasPendingLeakReport(List<Map<String, Object>> points) {
    List<Long> pointIds = points.stream()
        .map(point -> ((Number) point.get("id")).longValue())
        .toList();
    return leakReportRepo.findByPointIds(pointIds).stream()
        .anyMatch(report -> VerifyStatus.PENDING.name().equals(report.get("verify_status")));
  }
}
