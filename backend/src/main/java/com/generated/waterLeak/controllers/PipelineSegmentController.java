package com.generated.waterLeak.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.waterLeak.constructors.PipelineSegmentDtoFactory;
import com.generated.waterLeak.models.PipelineSegment;
import com.generated.waterLeak.services.PipelineSegmentService;
import com.generated.waterLeak.types.PipelineSegmentPayload;
import com.generated.waterLeak.types.RiskAdjustmentException;
import com.generated.waterLeak.types.RiskAdjustmentResult;

/**
 * 管网分段控制器。
 * 风险保存：PUT /api/pipeline-segment/risk —— 风险与巡检周期在同一请求内联动。
 */
@RestController
@RequestMapping("/api/pipeline-segment")
public class PipelineSegmentController {
  private final PipelineSegmentService service;

  public PipelineSegmentController(PipelineSegmentService service) {
    this.service = service;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }

  /** 管段下点位（含停用）与到期日，供管网页详情查看。 */
  @GetMapping("/{id}/inspection-points")
  public List<Map<String, Object>> listPoints(@PathVariable Long id) {
    return service.listPointsOfSegment(id);
  }

  /**
   * 保存管段风险：成功返回新风险、新周期及各启用点位新到期日；
   * 业务拒绝（待核实漏损阻止下调等）返回 4xx，前端据此保留原风险与原周期。
   */
  @PutMapping("/risk")
  public Map<String, Object> saveRisk(@RequestBody PipelineSegmentPayload payload) {
    RiskAdjustmentResult result = service.saveRiskLevel(payload);
    PipelineSegment latest = service.getSegment(result.segmentId());
    return PipelineSegmentDtoFactory.toRiskAdjustmentResponse(latest, result);
  }

  /** 控制器层包装：风险联动业务异常统一映射为 409/400，错误体带 code。 */
  @ExceptionHandler(RiskAdjustmentException.class)
  public ResponseEntity<Map<String, String>> handleRiskAdjustment(RiskAdjustmentException ex) {
    HttpStatus status = switch (ex.getCode()) {
      case "SEGMENT_NOT_FOUND" -> HttpStatus.NOT_FOUND;
      case "RISK_DOWNGRADE_BLOCKED" -> HttpStatus.CONFLICT;
      default -> HttpStatus.BAD_REQUEST;
    };
    return ResponseEntity.status(status)
        .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
  }
}
