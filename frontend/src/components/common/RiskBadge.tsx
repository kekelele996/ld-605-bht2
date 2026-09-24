import { StatusBadge } from "./StatusBadge";
import { formatRisk } from "../../utils/formatters";

export function RiskBadge({ title = "风险等级", value = "READY" }: { title?: string; value?: string }) {
  return (
    <span className="shared-widget risk-badge" title={title}>
      <StatusBadge value={value} label={formatRisk(value)} />
    </span>
  );
}
