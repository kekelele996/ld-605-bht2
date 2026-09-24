package com.generated.waterLeak.middlewares;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.services.RiskAdjustmentException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 全局错误处理：业务异常转 409，其余异常兜底 500，响应体统一为 {code, message}。 */
@RestControllerAdvice
public class ErrorHandlerMiddleware {

  @ExceptionHandler(RiskAdjustmentException.class)
  public ResponseEntity<Map<String, Object>> handleRiskAdjustment(RiskAdjustmentException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(body(ErrorCodes.VALIDATION_FAILED, ex.getMessage()));
  }

  private static Map<String, Object> body(String code, String message) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("code", code);
    body.put("message", message);
    return body;
  }
}
