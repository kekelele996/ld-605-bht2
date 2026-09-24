package com.generated.waterLeak.middlewares;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;

/**
 * 全局错误处理中间件：兜底处理控制器未覆盖的框架级异常。
 * 业务异常仍由各控制器先包装（service/controller 分别包装，禁止只在此处吞掉）。
 */
@RestControllerAdvice
public class ErrorHandlerMiddleware {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of(
            "code", ErrorCodes.VALIDATION_FAILED,
            "message", ErrorMessages.VALIDATION_FAILED));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("code", "INTERNAL_ERROR", "message", ex.getMessage() == null
            ? "服务暂时不可用" : ex.getMessage()));
  }
}
