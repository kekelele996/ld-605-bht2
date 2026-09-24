package com.generated.waterLeak.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 巡检周期与到期日计算。
 * 规则：新到期日 = 点位最近一次检查时间 + 风险对应周期天数
 * （低风险 30 天、中风险 14 天、高风险 7 天、极高风险 3 天）。
 */
public final class InspectionScheduleCalculator {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  private InspectionScheduleCalculator() {}

  /**
   * 以最近一次检查时间为起点，叠加 cycleDays 得到新到期日（UTC，ISO-8601）。
   *
   * @param lastCheckedAt 最近检查时间，形如 2026-09-20T09:00:00Z
   * @param cycleDays 巡检周期（天）
   * @throws IllegalArgumentException 检查时间缺失或无法解析时抛出
   */
  public static String calculateNextDueAt(String lastCheckedAt, int cycleDays) {
    if (lastCheckedAt == null || lastCheckedAt.isBlank()) {
      throw new IllegalArgumentException("last_checked_at is required");
    }
    LocalDate baseDate = parseDate(lastCheckedAt.trim());
    return baseDate.plusDays(cycleDays).atStartOfDay(ZoneOffset.UTC)
        .format(DATE_TIME_FORMATTER);
  }

  private static LocalDate parseDate(String value) {
    try {
      return LocalDate.from(DATE_TIME_FORMATTER.parse(value));
    } catch (DateTimeParseException ignored) {
      // 兼容仅日期（yyyy-MM-dd）与无时区的本地时间两种种子写法。
      try {
        return LocalDate.parse(value.length() > 10 ? value.substring(0, 10) : value,
            DATE_FORMATTER);
      } catch (DateTimeParseException ex) {
        throw new IllegalArgumentException("invalid last_checked_at: " + value, ex);
      }
    }
  }
}
