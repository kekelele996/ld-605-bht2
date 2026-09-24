import { useEffect, useState } from "react";
import { Alert, Button, message, Select, Table, Tag } from "antd";
import { RiskBadge } from "../components/common/RiskBadge";
import { RISK_FREQUENCY_DAYS, RiskLevel } from "../constants/RiskLevel";
import { useInspectionPointStore } from "../stores/InspectionPointStore";
import { usePipelineSegmentStore } from "../stores/PipelineSegmentStore";
import type { PipelineSegment } from "../types/PipelineSegment";
import type { PointScheduleEntry } from "../types/RiskSchedule";
import { formatDate, formatFrequencyDays, formatRisk } from "../utils/formatters";

export function PipelinesPage() {
  const { rows, loading, load, adjustRisk } = usePipelineSegmentStore();
  const points = useInspectionPointStore((state) => state.rows);
  const loadPoints = useInspectionPointStore((state) => state.load);
  const [drafts, setDrafts] = useState<Record<number, RiskLevel>>({});
  const [savingId, setSavingId] = useState<number | null>(null);
  const [schedule, setSchedule] = useState<{ segmentCode: string; entries: PointScheduleEntry[] } | null>(null);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    void load();
    void loadPoints();
  }, [load, loadPoints]);

  const onSave = async (segment: PipelineSegment) => {
    const current = segment.risk_level as RiskLevel;
    const next = drafts[segment.id] ?? current;
    if (next === current) {
      messageApi.info("风险等级未变化");
      return;
    }
    setSavingId(segment.id);
    try {
      const res = await adjustRisk(segment.id, next);
      setSchedule({ segmentCode: segment.segment_code, entries: res.points });
      messageApi.success(`风险已保存，巡检周期已联动为 ${formatFrequencyDays(RISK_FREQUENCY_DAYS[next])}`);
    } catch (err) {
      // 后端拒绝：store 未改动，本地保持原风险与原周期，下拉框也回退到原风险
      setDrafts((prev) => ({ ...prev, [segment.id]: current }));
      messageApi.error(err instanceof Error ? err.message : "保存失败，已保留原风险与原周期");
    } finally {
      setSavingId(null);
    }
  };

  const columns = [
    { title: "管段编码", dataIndex: "segment_code", key: "segment_code" },
    { title: "区域", dataIndex: "district", key: "district" },
    {
      title: "当前风险",
      dataIndex: "risk_level",
      key: "risk_level",
      render: (value: string) => <RiskBadge title="风险" value={formatRisk(value)} />
    },
    {
      title: "启用点位周期",
      key: "frequency",
      render: (_: unknown, record: PipelineSegment) => {
        const enabled = points.filter((point) => point.pipeline_segment_id === record.id && point.status === "ENABLED");
        if (enabled.length === 0) return <Tag>无启用点位</Tag>;
        return enabled.map((point) => (
          <Tag key={point.id}>{point.point_code}：{formatFrequencyDays(point.check_frequency)}</Tag>
        ));
      }
    },
    {
      title: "调整为",
      key: "draft",
      render: (_: unknown, record: PipelineSegment) => {
        const current = record.risk_level as RiskLevel;
        return (
          <Select
            style={{ width: 180 }}
            value={drafts[record.id] ?? current}
            onChange={(value: RiskLevel) => setDrafts((prev) => ({ ...prev, [record.id]: value }))}
            options={RiskLevel.map((level) => ({
              value: level,
              label: `${formatRisk(level)}（${formatFrequencyDays(RISK_FREQUENCY_DAYS[level])}）`
            }))}
          />
        );
      }
    },
    {
      title: "操作",
      key: "action",
      render: (_: unknown, record: PipelineSegment) => (
        <Button
          type="primary"
          loading={savingId === record.id}
          disabled={(drafts[record.id] ?? record.risk_level) === record.risk_level}
          onClick={() => void onSave(record)}
        >
          保存风险
        </Button>
      )
    }
  ];

  return (
    <main className="page">
      {contextHolder}
      <section className="page-head">
        <div>
          <p className="eyebrow">water-leak</p>
          <h1>管网资产</h1>
        </div>
      </section>
      <section className="panel wide">
        <h2>管段风险维护（保存后按 低30 / 中14 / 高7 / 极高3 天联动巡检周期）</h2>
        <Table<PipelineSegment>
          rowKey="id"
          loading={loading}
          dataSource={rows}
          columns={columns}
          pagination={false}
        />
      </section>
      {schedule && (
        <section className="panel wide">
          <Alert
            type="success"
            showIcon
            message={`管段 ${schedule.segmentCode} 风险已保存，各启用点位的新到期日如下（起点取最近一次检查时间）`}
          />
          {schedule.entries.length === 0 ? (
            <p>该管段当前没有启用的巡检点位。</p>
          ) : (
            <Table<PointScheduleEntry>
              rowKey="pointId"
              dataSource={schedule.entries}
              pagination={false}
              columns={[
                { title: "点位编码", dataIndex: "pointCode", key: "pointCode" },
                {
                  title: "新巡检周期",
                  dataIndex: "frequencyDays",
                  key: "frequencyDays",
                  render: (value: number) => formatFrequencyDays(value)
                },
                {
                  title: "最近检查时间",
                  dataIndex: "lastCheckedAt",
                  key: "lastCheckedAt",
                  render: (value: string) => formatDate(value)
                },
                {
                  title: "新到期日",
                  dataIndex: "nextDueAt",
                  key: "nextDueAt",
                  render: (value: string) => formatDate(value)
                }
              ]}
            />
          )}
        </section>
      )}
    </main>
  );
}
