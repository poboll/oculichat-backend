package com.caiths.oculichatback.common;

/**
 * 错误码枚举类，定义了系统中可能出现的各种错误码及其对应的消息。
 * <p>
 * 每个枚举常量都包含一个状态码和一条描述信息，用于在 API 响应中传递具体的错误信息。
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * // 在控制器中返回错误响应
 * @GetMapping("/example")
 * public BaseResponse<Void> example() {
 *     // 某种错误情况
 *     return BaseResponse.error(ErrorCode.PARAMS_ERROR);
 * }
 * }</pre>
 *
 * <p>适用范围：</p>
 * <ul>
 *     <li>所有需要标准化错误响应的 API 接口。</li>
 *     <li>服务内部错误处理和日志记录。</li>
 * </ul>
 *
 * @version 1.0
 * @since 2024-12-02
 */
public enum ErrorCode {

    /**
     * 成功。
     */
    SUCCESS(0, "ok"),

    /**
     * 请求参数错误。
     */
    PARAMS_ERROR(40000, "请求参数错误"),

    /**
     * 用户未登录。
     */
    NOT_LOGIN_ERROR(40100, "未登录"),

    /**
     * 无权限访问。
     */
    NO_AUTH_ERROR(40101, "无权限"),

    /**
     * 账号已被封禁。
     */
    PROHIBITED(40001, "账号已封禁"),

    /**
     * 请求的数据不存在。
     */
    NOT_FOUND_ERROR(40400, "请求数据不存在"),

    /**
     * 禁止访问。
     */
    FORBIDDEN_ERROR(40300, "禁止访问"),

    /**
     * 系统内部异常。
     */
    SYSTEM_ERROR(50000, "系统内部异常"),

    /**
     * 操作失败。
     */
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码，用于标识错误类型。
     */
    private final int code;

    /**
     * 错误信息，提供对错误的详细描述。
     */
    private final String message;

    /**
     * 构造方法，初始化错误码和错误信息。
     *
     * @param code    状态码
     * @param message 错误信息
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误的状态码。
     *
     * @return 错误的状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误的描述信息。
     *
     * @return 错误的描述信息
     */
    public String getMessage() {
        return message;
    }
}
