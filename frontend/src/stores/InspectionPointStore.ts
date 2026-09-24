import { create } from "zustand";
import { listInspectionPoint } from "../api/InspectionPoint";
import type { InspectionPoint } from "../types/InspectionPoint";
import type { PointScheduleEntry } from "../types/RiskSchedule";

type State = {
  rows: InspectionPoint[];
  loading: boolean;
  load: () => Promise<void>;
  /** 风险保存成功后，按后端返回把各启用点位的巡检周期刷成新值。 */
  applySchedule: (entries: PointScheduleEntry[]) => void;
};

export const useInspectionPointStore = create<State>((set) => ({
  rows: [],
  loading: false,
  async load() {
    set({ loading: true });
    set({ rows: await listInspectionPoint(), loading: false });
  },
  applySchedule(entries) {
    set((state) => ({
      rows: state.rows.map((row) => {
        const hit = entries.find((entry) => entry.pointId === row.id);
        return hit ? { ...row, check_frequency: String(hit.frequencyDays) } : row;
      })
    }));
  }
}));
