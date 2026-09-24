package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.generated.waterLeak.constants.VerifyStatus;
import com.generated.waterLeak.models.LeakReport;

/**
 * 漏损报告仓储（本地内存种子数据）。
 * 待核实（PENDING_VERIFY）漏损是风险禁止下调的唯一业务依据。
 */
@Repository
public class LeakReportRepository {
  private final Map<Long, LeakReport> store = new LinkedHashMap<>();

  public LeakReportRepository() {
    // 管段 3（点位 4）存在一条待核实漏损：该管段高风险不允许下调
    save(new LeakReport(1L, "RESIDENT", 4L, "MAJOR", "调压箱周边路面持续渗水",
        "2026-09-22T07:40:00Z", VerifyStatus.PENDING_VERIFY.name(), "/mock/leak-1.png"));
    // 管段 4（点位 6）漏损已核实并派单，不阻止风险调整
    save(new LeakReport(2L, "INSPECTOR", 6L, "MINOR", "井盖边缘轻微返潮",
        "2026-09-10T15:00:00Z", VerifyStatus.VERIFIED.name(), "/mock/leak-2.png"));
  }

  private void save(LeakReport report) {
    store.put(report.id, report);
  }

  public List<LeakReport> findAll() {
    return new ArrayList<>(store.values());
  }

  /** 判断给定管段下是否存在“待核实”漏损（按该管段点位上的报告聚合）。 */
  public boolean existsPendingVerifyBySegmentId(
      Long segmentId, List<Long> segmentPointIds) {
    for (LeakReport report : store.values()) {
      if (VerifyStatus.isPending(report.verify_status)
          && report.point_id != null
          && segmentPointIds.contains(report.point_id)) {
        return true;
      }
    }
    return false;
  }
}
