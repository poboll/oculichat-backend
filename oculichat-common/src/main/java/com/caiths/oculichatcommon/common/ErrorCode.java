package com.caiths.oculichatcommon.common;

/**
 * 错误码枚举，定义了系统中可能出现的各种错误及其对应的状态码和消息。
 *
 * @version 1.0
 * @date 2024-12-02
 * @author
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
     * 未登录。
     */
    NOT_LOGIN_ERROR(40100, "未登录"),

    /**
     * 无权限。
     */
    NO_AUTH_ERROR(40101, "无权限"),

    /**
     * 账号已封禁。
     */
    PROHIBITED(40001, "账号已封禁"),

    /**
     * 请求数据不存在。
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
     * 状态码，表示错误的具体类型。
     */
    private final int code;

    /**
     * 错误信息，描述错误的详细内容。
     */
    private final String message;

    /**
     * 构造一个新的 ErrorCode 枚举实例。
     *
     * @param code 状态码
     * @param message 错误信息
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码。
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误信息。
     *
     * @return 错误信息
     */
    public String getMessage() {
        return message;
    }
}
