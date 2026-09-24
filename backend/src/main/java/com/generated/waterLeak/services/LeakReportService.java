package com.generated.waterLeak.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.generated.waterLeak.constructors.LeakReportDtoFactory;
import com.generated.waterLeak.repositories.LeakReportRepository;

/** 漏损报告服务：待核实（PENDING_VERIFY）漏损是管段风险禁止下调的判定依据。 */
@Service
public class LeakReportService {
  private final LeakReportRepository repo;

  public LeakReportService(LeakReportRepository repo) {
    this.repo = repo;
  }

  public List<Map<String, Object>> list() {
    List<Map<String, Object>> rows = new ArrayList<>();
    repo.findAll().forEach(report -> rows.add(LeakReportDtoFactory.toListItem(report)));
    return rows;
  }
}
