import { create } from "zustand";
import { listPipelineSegment, savePipelineSegmentRisk } from "../api/PipelineSegment";
import type {
  PipelineSegment,
  RiskAdjustmentResult,
  RiskSavePayload
} from "../types/PipelineSegment";

type State = {
  rows: PipelineSegment[];
  loading: boolean;
  saving: boolean;
  /** 最近一次保存成功后各启用点位的新到期日清单，供管网页展示。 */
  lastResult: RiskAdjustmentResult | null;
  load: () => Promise<void>;
  saveRisk: (payload: RiskSavePayload) => Promise<RiskAdjustmentResult>;
  clearLastResult: () => void;
};

export const usePipelineSegmentStore = create<State>((set) => ({
  rows: [],
  loading: false,
  saving: false,
  lastResult: null,
  async load() {
    set({ loading: true });
    set({ rows: await listPipelineSegment(), loading: false });
  },
  async saveRisk(payload) {
    set({ saving: true });
    try {
      // 风险与巡检周期在后端同一事务内联动；成功才落本地状态。
      const result = await savePipelineSegmentRisk(payload);
      set((state) => ({
        rows: state.rows.map((row) =>
          row.id === result.segmentId
            ? { ...row, risk_level: result.riskLevel }
            : row
        ),
        lastResult: result,
        saving: false
      }));
      return result;
    } catch (error) {
      // 后端拒绝：不更新任何本地状态，原风险与原巡检周期原样保留。
      set({ saving: false });
      throw error;
    }
  },
  clearLastResult() {
    set({ lastResult: null });
  }
}));
