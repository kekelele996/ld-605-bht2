package com.generated.waterLeak.middlewares;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 操作日志中间件：所有写操作（风险调整、周期重排、拒绝事件）均需经此记录。
 * 当前为本地内存日志，结构与 audit_log 表对齐。
 */
@Component
public class AuditLogMiddleware {
  private static final Logger log = LoggerFactory.getLogger(AuditLogMiddleware.class);
  private final List<String> entries = new CopyOnWriteArrayList<>();

  public void record(String message) {
    String entry = Instant.now() + " " + message;
    entries.add(entry);
    log.info(entry);
  }

  public List<String> recent(int limit) {
    int from = Math.max(0, entries.size() - limit);
    return List.copyOf(entries.subList(from, entries.size()));
  }
}
