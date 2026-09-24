package com.generated.waterLeak.types;

/** 巡检点维护请求：周期配置随管段风险联动，也可单独维护点位基础信息。 */
public record InspectionPointPayload(
    Long id,
    Long pipeline_segment_id,
    String point_code,
    String point_type,
    String address_desc,
    String last_checked_at,
    String status) {}
