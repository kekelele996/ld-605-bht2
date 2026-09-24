package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.PipelineSegmentRoutes;
import com.generated.waterLeak.services.PipelineSegmentService;
import com.generated.waterLeak.types.RiskAdjustRequest;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PipelineSegmentRoutes.PATH)
public class PipelineSegmentController {
  private final PipelineSegmentService service;

  public PipelineSegmentController(PipelineSegmentService service) {
    this.service = service;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }

  /** 保存管段风险并联动巡检周期；业务拒绝由 ErrorHandlerMiddleware 转 409。 */
  @PostMapping(PipelineSegmentRoutes.RISK_SUFFIX)
  public Map<String, Object> adjustRisk(@RequestBody RiskAdjustRequest request) {
    return service.adjustRisk(request);
  }
}
