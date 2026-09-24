export function StatusBadge({ value, label }: { value: string; label?: string }) {
  const className = "badge " + String(value).toLowerCase().replace(/_/g, "-");
  return (
    <span className={className}>{label ?? String(value).replace(/_/g, " ")}</span>
  );
}
