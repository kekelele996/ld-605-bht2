package com.generated.waterLeak.services;

/** 风险调整业务异常：携带错误码，由 ErrorHandlerMiddleware 统一转成 409 响应。 */
public class RiskAdjustmentException extends RuntimeException {
  private final String code;

  public RiskAdjustmentException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
