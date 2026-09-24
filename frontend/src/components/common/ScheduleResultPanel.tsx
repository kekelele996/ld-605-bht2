import { RiskLevelText, RISK_CYCLE_DAYS } from "../../constants/RiskLevel";
import type { RiskAdjustmentResult } from "../../types/PipelineSegment";
import { formatDateDay, formatRisk } from "../../utils/formatters";

/**
 * 风险保存结果面板：列出该管段下各启用点位的新到期日。
 * 起点 = 点位最近一次检查时间；新到期日 = 起点 + 风险对应周期。
 */
export function ScheduleResultPanel({ result }: { result: RiskAdjustmentResult }) {
  const cycleDays = result.cycleDays;
  return (
    <div className="panel schedule-result" role="status">
      <h2>
        巡检周期已联动 · 管段 {result.segmentCode}（{formatRisk(result.riskLevel)} / {cycleDays} 天）
      </h2>
      <p className="schedule-hint">
        原风险 {formatRisk(result.previousRiskLevel)}
        （{RISK_CYCLE_DAYS[result.previousRiskLevel as keyof typeof RISK_CYCLE_DAYS] ?? "-"} 天）
        → {RiskLevelText[result.riskLevel as keyof typeof RiskLevelText] ?? result.riskLevel}
        （{cycleDays} 天），以下为各启用点位的新到期日：
      </p>
      {result.enabledPoints.length === 0 ? (
        <p className="schedule-empty">该管段暂无启用点位，未生成新到期日。</p>
      ) : (
        <div className="table">
          <div className="row schedule-row schedule-head">
            <span>点位编号</span>
            <span>周期</span>
            <span>最近检查时间（起点）</span>
            <span>新到期日</span>
          </div>
          {result.enabledPoints.map((point) => (
            <div className="row schedule-row" key={point.pointId}>
              <strong>{point.pointCode}</strong>
              <span>{point.cycleDays} 天</span>
              <span>{formatDateDay(point.lastCheckedAt)}</span>
              <span className="due-date">{formatDateDay(point.nextDueAt)}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
