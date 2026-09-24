/** 后端业务拒绝时抛出的错误，携带错误码供页面提示。 */
export class ApiError extends Error {
  readonly code: string;

  constructor(code: string, message: string) {
    super(message);
    this.name = "ApiError";
    this.code = code;
  }
}
