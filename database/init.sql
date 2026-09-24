CREATE TABLE IF NOT EXISTS pipeline_segment (
  id INTEGER PRIMARY KEY,
  segment_code TEXT,
  district TEXT,
  material TEXT,
  diameter TEXT,
  install_year TEXT,
  pressure_zone TEXT,
  risk_level TEXT
);

CREATE TABLE IF NOT EXISTS inspection_point (
  id INTEGER PRIMARY KEY,
  pipeline_segment_id INTEGER,
  point_code TEXT,
  point_type TEXT,
  address_desc TEXT,
  check_frequency INTEGER,
  last_checked_at TEXT,
  next_due_at TEXT,
  status TEXT
);

CREATE TABLE IF NOT EXISTS leak_report (
  id INTEGER PRIMARY KEY,
  reporter_type TEXT,
  point_id INTEGER,
  leak_level TEXT,
  description TEXT,
  reported_at TEXT,
  verify_status TEXT,
  photo_url TEXT
);

CREATE TABLE IF NOT EXISTS repair_order (
  id INTEGER PRIMARY KEY,
  leak_report_id TEXT,
  crew_id TEXT,
  priority TEXT,
  status TEXT,
  planned_start TEXT,
  finished_at TEXT,
  cost_amount TEXT
);

CREATE TABLE IF NOT EXISTS material_usage (
  id INTEGER PRIMARY KEY,
  repair_order_id TEXT,
  material_code TEXT,
  material_name TEXT,
  quantity TEXT,
  unit TEXT,
  warehouse TEXT,
  usage_status TEXT
);

CREATE TABLE IF NOT EXISTS audit_log (
  id INTEGER PRIMARY KEY,
  actor TEXT,
  action TEXT,
  target_type TEXT,
  target_id TEXT,
  created_at TEXT
);

-- 风险与巡检周期联动种子数据：
-- 周期口径 低风险30天 / 中风险14天 / 高风险7天 / 极高风险3天，
-- next_due_at = last_checked_at + check_frequency。
INSERT INTO pipeline_segment (id, segment_code, district, material, diameter, install_year, pressure_zone, risk_level)
VALUES
  (1, 'PN-2026-001', '江岸区', '球墨铸铁', 'DN300', '2014', '中压区', 'LOW'),
  (2, 'PN-2026-002', '江汉区', '钢管', 'DN500', '2009', '高压区', 'MEDIUM'),
  (3, 'PN-2026-003', '硚口区', 'PE 管', 'DN200', '2018', '中压区', 'HIGH'),
  (4, 'PN-2026-004', '汉阳区', '灰口铸铁', 'DN400', '2001', '低压区', 'HIGH')
ON CONFLICT (id) DO NOTHING;

INSERT INTO inspection_point (id, pipeline_segment_id, point_code, point_type, address_desc, check_frequency, last_checked_at, next_due_at, status)
VALUES
  (1, 1, 'PT-001-01', '闸阀井', '江岸区沿江大道 12 号', 30, '2026-09-01T09:00:00Z', '2026-10-01T00:00:00Z', 'ENABLED'),
  (2, 1, 'PT-001-02', '消火栓', '江岸区卢沟桥路 5 号', 30, '2026-08-20T14:00:00Z', '2026-09-19T00:00:00Z', 'ENABLED'),
  (3, 2, 'PT-002-01', '排气阀', '江汉区中山大道 88 号', 14, '2026-09-10T08:30:00Z', '2026-09-24T00:00:00Z', 'ENABLED'),
  (4, 3, 'PT-003-01', '调压箱', '硚口区解放大道 200 号', 7, '2026-09-21T10:00:00Z', '2026-09-28T00:00:00Z', 'ENABLED'),
  (5, 3, 'PT-003-02', '流量计', '硚口区古田二路 17 号', 7, '2026-09-19T16:45:00Z', '2026-09-26T00:00:00Z', 'ENABLED'),
  (6, 4, 'PT-004-01', '检查井', '汉阳区琴台大道 66 号', 7, '2026-09-18T11:20:00Z', '2026-09-25T00:00:00Z', 'ENABLED'),
  (7, 4, 'PT-004-02', '废弃水尺', '汉阳区龙灯堤（围挡内）', 7, '2026-08-01T09:00:00Z', '2026-08-08T00:00:00Z', 'DISABLED')
ON CONFLICT (id) DO NOTHING;

-- 管段3（点位4）存在待核实漏损：该管段风险不允许下调；
-- 管段4（点位6）漏损已核实，不阻止风险调整。
INSERT INTO leak_report (id, reporter_type, point_id, leak_level, description, reported_at, verify_status, photo_url)
VALUES
  (1, 'RESIDENT', 4, 'MAJOR', '调压箱周边路面持续渗水', '2026-09-22T07:40:00Z', 'PENDING_VERIFY', '/mock/leak-1.png'),
  (2, 'INSPECTOR', 6, 'MINOR', '井盖边缘轻微返潮', '2026-09-10T15:00:00Z', 'VERIFIED', '/mock/leak-2.png')
ON CONFLICT (id) DO NOTHING;
