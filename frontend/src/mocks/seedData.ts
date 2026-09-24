export const mockData = {
  "pipelineSegment": [
    {
      "id": 1,
      "segment_code": "PN-2026-001",
      "district": "江岸区",
      "material": "球墨铸铁",
      "diameter": "DN300",
      "install_year": "2014",
      "pressure_zone": "中压区",
      "risk_level": "LOW",
      "enabled_point_count": 2
    },
    {
      "id": 2,
      "segment_code": "PN-2026-002",
      "district": "江汉区",
      "material": "钢管",
      "diameter": "DN500",
      "install_year": "2009",
      "pressure_zone": "高压区",
      "risk_level": "MEDIUM",
      "enabled_point_count": 1
    },
    {
      "id": 3,
      "segment_code": "PN-2026-003",
      "district": "硚口区",
      "material": "PE 管",
      "diameter": "DN200",
      "install_year": "2018",
      "pressure_zone": "中压区",
      "risk_level": "HIGH",
      "enabled_point_count": 2
    },
    {
      "id": 4,
      "segment_code": "PN-2026-004",
      "district": "汉阳区",
      "material": "灰口铸铁",
      "diameter": "DN400",
      "install_year": "2001",
      "pressure_zone": "低压区",
      "risk_level": "HIGH",
      "enabled_point_count": 1
    }
  ],
  "inspectionPoint": [
    {
      "id": 1,
      "pipeline_segment_id": 1,
      "point_code": "PT-001-01",
      "point_type": "闸阀井",
      "address_desc": "江岸区沿江大道 12 号",
      "check_frequency": 30,
      "last_checked_at": "2026-09-01T09:00:00Z",
      "next_due_at": "2026-10-01T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 2,
      "pipeline_segment_id": 1,
      "point_code": "PT-001-02",
      "point_type": "消火栓",
      "address_desc": "江岸区卢沟桥路 5 号",
      "check_frequency": 30,
      "last_checked_at": "2026-08-20T14:00:00Z",
      "next_due_at": "2026-09-19T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 3,
      "pipeline_segment_id": 2,
      "point_code": "PT-002-01",
      "point_type": "排气阀",
      "address_desc": "江汉区中山大道 88 号",
      "check_frequency": 14,
      "last_checked_at": "2026-09-10T08:30:00Z",
      "next_due_at": "2026-09-24T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 4,
      "pipeline_segment_id": 3,
      "point_code": "PT-003-01",
      "point_type": "调压箱",
      "address_desc": "硚口区解放大道 200 号",
      "check_frequency": 7,
      "last_checked_at": "2026-09-21T10:00:00Z",
      "next_due_at": "2026-09-28T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 5,
      "pipeline_segment_id": 3,
      "point_code": "PT-003-02",
      "point_type": "流量计",
      "address_desc": "硚口区古田二路 17 号",
      "check_frequency": 7,
      "last_checked_at": "2026-09-19T16:45:00Z",
      "next_due_at": "2026-09-26T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 6,
      "pipeline_segment_id": 4,
      "point_code": "PT-004-01",
      "point_type": "检查井",
      "address_desc": "汉阳区琴台大道 66 号",
      "check_frequency": 7,
      "last_checked_at": "2026-09-18T11:20:00Z",
      "next_due_at": "2026-09-25T00:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 7,
      "pipeline_segment_id": 4,
      "point_code": "PT-004-02",
      "point_type": "废弃水尺",
      "address_desc": "汉阳区龙灯堤（围挡内）",
      "check_frequency": 7,
      "last_checked_at": "2026-08-01T09:00:00Z",
      "next_due_at": "2026-08-08T00:00:00Z",
      "status": "DISABLED"
    }
  ],
  "leakReport": [
    {
      "id": 1,
      "reporter_type": "RESIDENT",
      "point_id": 4,
      "leak_level": "MAJOR",
      "description": "调压箱周边路面持续渗水",
      "reported_at": "2026-09-22T07:40:00Z",
      "verify_status": "PENDING_VERIFY",
      "photo_url": "/mock/leak-1.png"
    },
    {
      "id": 2,
      "reporter_type": "INSPECTOR",
      "point_id": 6,
      "leak_level": "MINOR",
      "description": "井盖边缘轻微返潮",
      "reported_at": "2026-09-10T15:00:00Z",
      "verify_status": "VERIFIED",
      "photo_url": "/mock/leak-2.png"
    }
  ],
  "repairOrder": [
    {
      "id": 1,
      "leak_report_id": 1,
      "crew_id": 1,
      "priority": "priority 1",
      "status": "ASSIGNED",
      "planned_start": "planned start 1",
      "finished_at": "2026-06-11T09:00:00Z",
      "cost_amount": 15400
    },
    {
      "id": 2,
      "leak_report_id": 2,
      "crew_id": 2,
      "priority": "priority 2",
      "status": "WORKING",
      "planned_start": "planned start 2",
      "finished_at": "2026-06-12T09:00:00Z",
      "cost_amount": 18800
    },
    {
      "id": 3,
      "leak_report_id": 3,
      "crew_id": 3,
      "priority": "priority 3",
      "status": "WAIT_ASSIGN",
      "planned_start": "planned start 3",
      "finished_at": "2026-06-13T09:00:00Z",
      "cost_amount": 22200
    }
  ],
  "materialUsage": [
    {
      "id": 1,
      "repair_order_id": 1,
      "material_code": "material code 1",
      "material_name": "material name 1",
      "quantity": 92,
      "unit": "unit 1",
      "warehouse": "warehouse 1",
      "usage_status": "ASSIGNED"
    },
    {
      "id": 2,
      "repair_order_id": 2,
      "material_code": "material code 2",
      "material_name": "material name 2",
      "quantity": 104,
      "unit": "unit 2",
      "warehouse": "warehouse 2",
      "usage_status": "WORKING"
    },
    {
      "id": 3,
      "repair_order_id": 3,
      "material_code": "material code 3",
      "material_name": "material name 3",
      "quantity": 116,
      "unit": "unit 3",
      "warehouse": "warehouse 3",
      "usage_status": "WAIT_ASSIGN"
    }
  ]
} as const;
