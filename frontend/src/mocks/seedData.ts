export const mockData = {
  "pipelineSegment": [
    {
      "id": 1,
      "segment_code": "SEG-001",
      "district": "城东",
      "material": "球墨铸铁",
      "diameter": "DN300",
      "install_year": "1998",
      "pressure_zone": "PZ-1",
      "risk_level": "LOW"
    },
    {
      "id": 2,
      "segment_code": "SEG-002",
      "district": "城西",
      "material": "钢管",
      "diameter": "DN500",
      "install_year": "2005",
      "pressure_zone": "PZ-2",
      "risk_level": "MEDIUM"
    },
    {
      "id": 3,
      "segment_code": "SEG-003",
      "district": "江北",
      "material": "PE",
      "diameter": "DN200",
      "install_year": "2012",
      "pressure_zone": "PZ-1",
      "risk_level": "HIGH"
    },
    {
      "id": 4,
      "segment_code": "SEG-004",
      "district": "江南",
      "material": "铸铁",
      "diameter": "DN400",
      "install_year": "1986",
      "pressure_zone": "PZ-3",
      "risk_level": "EXTREME"
    },
    {
      "id": 5,
      "segment_code": "SEG-005",
      "district": "高新",
      "material": "钢管",
      "diameter": "DN600",
      "install_year": "2016",
      "pressure_zone": "PZ-2",
      "risk_level": "MEDIUM"
    }
  ],
  "inspectionPoint": [
    {
      "id": 1,
      "pipeline_segment_id": 1,
      "point_code": "PT-1001",
      "point_type": "阀门井",
      "address_desc": "城东路 12 号",
      "check_frequency": "30",
      "last_checked_at": "2026-09-10T08:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 2,
      "pipeline_segment_id": 2,
      "point_code": "PT-1002",
      "point_type": "流量计",
      "address_desc": "城西大道 88 号",
      "check_frequency": "14",
      "last_checked_at": "2026-09-12T08:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 3,
      "pipeline_segment_id": 3,
      "point_code": "PT-1003",
      "point_type": "消火栓",
      "address_desc": "江北街 5 号",
      "check_frequency": "7",
      "last_checked_at": "2026-09-15T08:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 4,
      "pipeline_segment_id": 3,
      "point_code": "PT-1004",
      "point_type": "排气阀",
      "address_desc": "江北街 9 号",
      "check_frequency": "7",
      "last_checked_at": "2026-09-01T08:00:00Z",
      "status": "DISABLED"
    },
    {
      "id": 5,
      "pipeline_segment_id": 4,
      "point_code": "PT-1005",
      "point_type": "阀门井",
      "address_desc": "江南路 30 号",
      "check_frequency": "3",
      "last_checked_at": "2026-09-20T08:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 6,
      "pipeline_segment_id": 5,
      "point_code": "PT-1006",
      "point_type": "流量计",
      "address_desc": "高新大道 101 号",
      "check_frequency": "14",
      "last_checked_at": "2026-09-18T08:00:00Z",
      "status": "ENABLED"
    },
    {
      "id": 7,
      "pipeline_segment_id": 5,
      "point_code": "PT-1007",
      "point_type": "听漏点",
      "address_desc": "高新大道 150 号",
      "check_frequency": "14",
      "last_checked_at": "2026-09-19T08:00:00Z",
      "status": "ENABLED"
    }
  ],
  "leakReport": [
    {
      "id": 1,
      "reporter_type": "RESIDENT",
      "point_id": 2,
      "leak_level": "MINOR",
      "description": "城西大道井盖渗水",
      "reported_at": "2026-09-13T10:00:00Z",
      "verify_status": "PENDING",
      "photo_url": "/mock/leak-1.png"
    },
    {
      "id": 2,
      "reporter_type": "INSPECTOR",
      "point_id": 3,
      "leak_level": "MAJOR",
      "description": "江北街管段接口渗漏",
      "reported_at": "2026-09-16T09:30:00Z",
      "verify_status": "CONFIRMED",
      "photo_url": "/mock/leak-2.png"
    },
    {
      "id": 3,
      "reporter_type": "RESIDENT",
      "point_id": 6,
      "leak_level": "BURST",
      "description": "高新大道疑似爆管",
      "reported_at": "2026-09-21T07:45:00Z",
      "verify_status": "PENDING",
      "photo_url": "/mock/leak-3.png"
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
