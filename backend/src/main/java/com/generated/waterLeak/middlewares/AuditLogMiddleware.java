package com.generated.waterLeak.middlewares;

/** 操作日志中间件：各 service 的写操作统一经此落审计日志。 */
public class AuditLogMiddleware {
  public static void record(String template, String detail) {
    System.out.println("[AUDIT] " + template + " | " + detail);
  }
}
