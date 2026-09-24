import { create } from "zustand";
import { adjustSegmentRisk, listPipelineSegment } from "../api/PipelineSegment";
import type { RiskLevel } from "../constants/RiskLevel";
import type { PipelineSegment } from "../types/PipelineSegment";
import type { SegmentRiskScheduleResponse } from "../types/RiskSchedule";
import { useInspectionPointStore } from "./InspectionPointStore";

type State = {
  rows: PipelineSegment[];
  loading: boolean;
  load: () => Promise<void>;
  /**
   * 保存管段风险并联动巡检周期。
   * 只有后端确认后才更新本地风险与点位周期；后端拒绝时抛错，本地保持原风险与原周期。
   */
  adjustRisk: (segmentId: number, riskLevel: RiskLevel) => Promise<SegmentRiskScheduleResponse>;
};

export const usePipelineSegmentStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listPipelineSegment(), loading: false });
  },
  async adjustRisk(segmentId, riskLevel) {
    const res = await adjustSegmentRisk(segmentId, riskLevel);
    set((state) => ({
      rows: state.rows.map((row) => (row.id === segmentId ? { ...row, risk_level: res.segment.risk_level } : row))
    }));
    useInspectionPointStore.getState().applySchedule(res.points);
    return res;
  }
}));
