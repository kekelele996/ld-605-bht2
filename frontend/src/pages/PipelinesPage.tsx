import { useEffect, useMemo, useState } from "react";
import { usePipelineSegmentStore } from "../stores/PipelineSegmentStore";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { RiskLevel, RiskLevelText, RISK_CYCLE_DAYS } from "../constants/RiskLevel";
import { LOG_TEMPLATES } from "../constants/logTemplates";
import { ApiRequestError } from "../api/PipelineSegment";
import type { RiskLevel as RiskLevelType } from "../constants/RiskLevel";
import type { PipelineSegment } from "../types/PipelineSegment";
import { formatDateDay, formatPointStatus, formatRisk } from "../utils/formatters";
import { RiskBadge } from "../components/common/RiskBadge";
import { ScheduleResultPanel } from "../components/common/ScheduleResultPanel";

/**
 * 管网资产页：班组长调整管段风险并保存。
 * 风险保存与巡检周期在后端同一事务联动；后端拒绝时前端保留原风险与原周期。
 */
export function PipelinesPage() {
  const { rows, loading, saving, lastResult, load, saveRisk, clearLastResult } =
    usePipelineSegmentStore();
  const { rows: points, load: loadPoints } = useInspectionPointStore();

  const [selectedId, setSelectedId] = useState<number | null>(null);
  // 风险调整草稿：后端拒绝时不回写 store，草稿由用户决定是否继续修改。
  const [draftRisk, setDraftRisk] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<string>("");

  useEffect(() => {
    void load();
    void loadPoints();
  }, [load, loadPoints]);

  const selected = useMemo(
    () => rows.find((segment) => segment.id === selectedId) ?? null,
    [rows, selectedId]
  );

  // 管段当前真实风险（来自 store），用于判断“是否下调”和展示保留的原周期。
  const originalRisk = selected?.risk_level ?? "";
  const pointsOfSegment = useMemo(
    () => points.filter((point) => point.pipeline_segment_id === selectedId),
    [points, selectedId]
  );
  // 该管段是否存在待核实漏损：前端与后端种子数据同源（后端 409 才是最终裁决）。
  const hasPendingLeak = PENDING_LEAK_SEGMENTS.has(selectedId ?? -1);

  const selectSegment = (segment: PipelineSegment) => {
    setSelectedId(segment.id);
    setDraftRisk(segment.risk_level);
    setErrorMessage("");
    clearLastResult();
  };

  const isDowngrade =
    !!originalRisk &&
    !!draftRisk &&
    RiskLevel.indexOf(draftRisk as RiskLevelType) <
      RiskLevel.indexOf(originalRisk as RiskLevelType);
  const blockedByPendingLeak = isDowngrade && hasPendingLeak;

  const handleSave = async () => {
    if (!selected) {
      return;
    }
    setErrorMessage("");
    try {
      // 保存成功：store 更新风险，后端返回各启用点位新到期日。
      const result = await saveRisk({ id: selected.id, risk_level: draftRisk });
      setDraftRisk(result.riskLevel);
      // 周期已在后端联动落库，刷新点位列表以展示新周期/到期日。
      await loadPoints();
    } catch (error) {
      // 后端拒绝（如待核实漏损阻止下调）：store 未改动，原风险与原周期保留。
      if (error instanceof ApiRequestError) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("保存失败：网络异常，原风险与原巡检周期已保留");
      }
      // 草稿恢复为后端当前（原始）风险，避免页面停留在被拒的低风险选择上。
      setDraftRisk(originalRisk);
    }
  };

  return (
    <section className="pipelines-page">
      <header className="page-head">
        <div>
          <p className="eyebrow">water-leak / pipeline assets</p>
          <h1>管网资产</h1>
          <p className="page-desc">
            调整管段风险并保存后，巡检周期自动联动：低风险 30 天、中风险 14 天、高风险 7 天、极高风险 3 天，
            起点取点位最近一次检查时间。
          </p>
        </div>
        <RiskBadge value={selected?.risk_level ?? "LOW"} />
      </header>

      <div className="workbench pipelines-layout">
        <div className="panel wide">
          <h2>管段列表（点击选择后调整风险）</h2>
          {loading ? (
            <p>加载中…</p>
          ) : (
            <div className="table">
              {rows.map((segment) => (
                <button
                  type="button"
                  key={segment.id}
                  className={
                    "row segment-row" + (segment.id === selectedId ? " selected" : "")
                  }
                  onClick={() => selectSegment(segment)}
                >
                  <strong>{segment.segment_code}</strong>
                  <span>
                    {segment.district} · {segment.material} {segment.diameter}
                  </span>
                  <span className="segment-risk">
                    <RiskBadge value={segment.risk_level} />
                    <em className="cycle-tag">
                      周期 {RISK_CYCLE_DAYS[segment.risk_level as RiskLevelType] ?? "-"} 天
                    </em>
                    {PENDING_LEAK_SEGMENTS.has(segment.id) && (
                      <em className="pending-tag">有待核实漏损</em>
                    )}
                  </span>
                </button>
              ))}
            </div>
          )}
        </div>

        <div className="panel risk-editor">
          <h2>风险调整</h2>
          {!selected ? (
            <p className="empty">请先在左侧选择一条管段。</p>
          ) : (
            <>
              <p className="editor-meta">
                管段 <strong>{selected.segment_code}</strong> · 当前风险：
                {formatRisk(originalRisk)}（周期{" "}
                {RISK_CYCLE_DAYS[originalRisk as RiskLevelType] ?? "-"} 天）
              </p>
              <label className="field">
                <span>新风险等级</span>
                <select
                  value={draftRisk}
                  onChange={(event) => {
                    setDraftRisk(event.target.value);
                    setErrorMessage("");
                  }}
                >
                  {RiskLevel.map((level) => (
                    <option key={level} value={level}>
                      {RiskLevelText[level]}（{RISK_CYCLE_DAYS[level]} 天）
                    </option>
                  ))}
                </select>
              </label>

              <p className="cycle-preview">
                保存后启用点位将按 {RISK_CYCLE_DAYS[draftRisk as RiskLevelType] ?? "-"} 天周期重排，
                起点为各点位最近一次检查时间，停用点位不参与。
              </p>
              {blockedByPendingLeak && (
                <p className="warning-text">
                  该管段存在待核实漏损，风险不允许下降；如强行保存后端将拒绝并保留原风险与原周期。
                </p>
              )}
              {errorMessage && <p className="error-text">{errorMessage}</p>}

              <button
                type="button"
                className="save-btn"
                disabled={saving || draftRisk === originalRisk}
                onClick={() => void handleSave()}
              >
                {saving ? "保存中…" : "保存风险并联动巡检周期"}
              </button>
              <p className="log-hint">{LOG_TEMPLATES.PipelineSegment[4]}</p>
            </>
          )}
        </div>
      </div>

      {lastResult && (
        <ScheduleResultPanel result={lastResult} />
      )}

      {selected && (
        <div className="panel">
          <h2>管段 {selected.segment_code} 的巡检点位（{pointsOfSegment.length}）</h2>
          <div className="table">
            <div className="row schedule-row schedule-head">
              <span>点位编号</span>
              <span>类型 / 地址</span>
              <span>状态</span>
              <span>周期（天）</span>
              <span>最近检查时间</span>
              <span>当前到期日</span>
            </div>
            {pointsOfSegment.map((point) => (
              <div className="row schedule-row" key={point.id}>
                <strong>{point.point_code}</strong>
                <span>
                  {point.point_type} · {point.address_desc}
                </span>
                <span>{formatPointStatus(point.status)}</span>
                <span>{point.check_frequency}</span>
                <span>{formatDateDay(point.last_checked_at)}</span>
                <span className="due-date">
                  {point.next_due_at ? formatDateDay(point.next_due_at) : "-"}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}
    </section>
  );
}

/**
 * 待核实漏损管段清单：与后端种子数据同源（管段 3 的点 PT-003-01 有 PENDING_VERIFY 漏损）。
 * 后端是最终裁决方；前端仅用于提前提示，真正的拒绝仍以后端 409 为准。
 */
const PENDING_LEAK_SEGMENTS = new Set<number>([3]);
