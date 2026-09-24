package com.generated.waterLeak.repositories;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.generated.waterLeak.constants.PointStatus;
import com.generated.waterLeak.models.InspectionPoint;

/**
 * 巡检点仓储（本地内存种子数据）。
 * 风险联动时仅 rescheduleEnabledPointsBySegment 更新启用点位的周期与到期日。
 */
@Repository
public class InspectionPointRepository {
  private final Map<Long, InspectionPoint> store = new LinkedHashMap<>();

  public InspectionPointRepository() {
    // 管段 1：低风险示例（周期 30 天）
    save(new InspectionPoint(1L, 1L, "PT-001-01", "闸阀井", "江岸区沿江大道 12 号",
        30, "2026-09-01T09:00:00Z", "2026-10-01T00:00:00Z", PointStatus.ENABLED.name()));
    save(new InspectionPoint(2L, 1L, "PT-001-02", "消火栓", "江岸区卢沟桥路 5 号",
        30, "2026-08-20T14:00:00Z", "2026-09-19T00:00:00Z", PointStatus.ENABLED.name()));
    // 管段 2：中风险示例（周期 14 天）
    save(new InspectionPoint(3L, 2L, "PT-002-01", "排气阀", "江汉区中山大道 88 号",
        14, "2026-09-10T08:30:00Z", "2026-09-24T00:00:00Z", PointStatus.ENABLED.name()));
    // 管段 3：高风险示例（周期 7 天），且存在待核实漏损，禁止下调
    save(new InspectionPoint(4L, 3L, "PT-003-01", "调压箱", "硚口区解放大道 200 号",
        7, "2026-09-21T10:00:00Z", "2026-09-28T00:00:00Z", PointStatus.ENABLED.name()));
    save(new InspectionPoint(5L, 3L, "PT-003-02", "流量计", "硚口区古田二路 17 号",
        7, "2026-09-19T16:45:00Z", "2026-09-26T00:00:00Z", PointStatus.ENABLED.name()));
    // 管段 4：高风险示例（周期 7 天），无待核实漏损，可下调
    save(new InspectionPoint(6L, 4L, "PT-004-01", "检查井", "汉阳区琴台大道 66 号",
        7, "2026-09-18T11:20:00Z", "2026-09-25T00:00:00Z", PointStatus.ENABLED.name()));
    // 停用点位：不参与周期重排，也不出现在新到期日清单中
    save(new InspectionPoint(7L, 4L, "PT-004-02", "废弃水尺", "汉阳区龙灯堤（围挡内）",
        7, "2026-08-01T09:00:00Z", "2026-08-08T00:00:00Z", PointStatus.DISABLED.name()));
  }

  private void save(InspectionPoint point) {
    store.put(point.id, point);
  }

  public List<InspectionPoint> findAll() {
    return new ArrayList<>(store.values());
  }

  public List<InspectionPoint> findBySegmentId(Long segmentId) {
    List<InspectionPoint> rows = new ArrayList<>();
    for (InspectionPoint point : store.values()) {
      if (segmentId.equals(point.pipeline_segment_id)) {
        rows.add(point);
      }
    }
    return rows;
  }

  /** 仅返回某管段下启用点位（风险保存后列出各启用点位的新到期日）。 */
  public List<InspectionPoint> findEnabledBySegmentId(Long segmentId) {
    return findBySegmentId(segmentId).stream()
        .filter(point -> PointStatus.isEnabled(point.status))
        .toList();
  }

  /** 批量落库新周期与新到期日。 */
  public void rescheduleEnabledPointsBySegment(
      Long segmentId, int cycleDays, java.util.function.Function<InspectionPoint, String> nextDueAtFn) {
    for (InspectionPoint point : findEnabledBySegmentId(segmentId)) {
      point.check_frequency = cycleDays;
      point.next_due_at = nextDueAtFn.apply(point);
    }
  }
}
