/** 后端业务错误：与后端 {code, message} 错误体对齐。 */
export interface ApiError {
  code: string;
  message: string;
}
