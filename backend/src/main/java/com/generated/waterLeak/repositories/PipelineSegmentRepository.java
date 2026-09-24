package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.generated.waterLeak.models.PipelineSegment;

/**
 * 管网分段仓储（本地内存种子数据；结构与 database/init.sql 对齐）。
 * 风险保存必须走 updateRiskLevel，禁止在 service 中散写更新逻辑。
 */
@Repository
public class PipelineSegmentRepository {
  private final Map<Long, PipelineSegment> store = new LinkedHashMap<>();

  public PipelineSegmentRepository() {
    save(new PipelineSegment(1L, "PN-2026-001", "江岸区", "球墨铸铁", "DN300",
        "2014", "中压区", "LOW"));
    save(new PipelineSegment(2L, "PN-2026-002", "江汉区", "钢管", "DN500",
        "2009", "高压区", "MEDIUM"));
    save(new PipelineSegment(3L, "PN-2026-003", "硚口区", "PE 管", "DN200",
        "2018", "中压区", "HIGH"));
    save(new PipelineSegment(4L, "PN-2026-004", "汉阳区", "灰口铸铁", "DN400",
        "2001", "低压区", "HIGH"));
  }

  private void save(PipelineSegment segment) {
    store.put(segment.id, segment);
  }

  public List<PipelineSegment> findAll() {
    return new ArrayList<>(store.values());
  }

  public Optional<PipelineSegment> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  /** 风险等级落库；返回更新后的管段快照。 */
  public PipelineSegment updateRiskLevel(Long id, String riskLevel) {
    PipelineSegment segment = store.get(id);
    if (segment == null) {
      throw new IllegalArgumentException("segment not found: " + id);
    }
    segment.risk_level = riskLevel;
    return segment;
  }
}
