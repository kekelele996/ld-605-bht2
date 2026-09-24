package com.generated.waterLeak.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.generated.waterLeak.constructors.InspectionPointDtoFactory;
import com.generated.waterLeak.repositories.InspectionPointRepository;

/** 巡检点服务：周期与到期日随管段风险联动，本服务负责只读展示与点位维护。 */
@Service
public class InspectionPointService {
  private final InspectionPointRepository repo;

  public InspectionPointService(InspectionPointRepository repo) {
    this.repo = repo;
  }

  public List<Map<String, Object>> list() {
    List<Map<String, Object>> rows = new ArrayList<>();
    repo.findAll().forEach(point -> rows.add(InspectionPointDtoFactory.toListItem(point)));
    return rows;
  }

  public List<Map<String, Object>> listBySegment(Long segmentId) {
    List<Map<String, Object>> rows = new ArrayList<>();
    repo.findBySegmentId(segmentId)
        .forEach(point -> rows.add(InspectionPointDtoFactory.toListItem(point)));
    return rows;
  }
}
