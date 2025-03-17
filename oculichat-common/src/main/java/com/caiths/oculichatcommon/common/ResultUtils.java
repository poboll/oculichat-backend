package com.caiths.oculichatcommon.common;

/**
 * 返回工具类，提供静态方法用于快速生成 {@link BaseResponse} 对象。
 *
 * <p>该工具类简化了常见的响应结果的创建过程，如成功响应、错误响应等。</p>
 *
 * @version 1.0
 * @date 2024-12-02
 * @author
 */
public class ResultUtils {

    /**
     * 创建一个成功的响应结果。
     *
     * @param <T>  返回数据的类型
     * @param data 返回的数据
     * @return 封装了成功状态码、数据和消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 创建一个基于错误码的错误响应结果。
     *
     * @param <T>        返回数据的类型
     * @param errorCode 错误码枚举
     * @return 封装了错误状态码和消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 创建一个包含数据和消息的错误响应结果。
     *
     * @param <T>     返回数据的类型
     * @param data    返回的数据
     * @param message 消息说明
     * @return 封装了状态码、数据和消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, String message) {
        return new BaseResponse<>(202, data, message);
    }

    /**
     * 创建一个包含状态码和消息的错误响应结果。
     *
     * @param <T>     返回数据的类型
     * @param code    状态码
     * @param message 消息说明
     * @return 封装了状态码和消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 创建一个包含数据和错误码的错误响应结果。
     *
     * @param <T>        返回数据的类型
     * @param data       返回的数据
     * @param errorCode  错误码枚举
     * @return 封装了错误状态码、数据和消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), data, errorCode.getMessage());
    }

    /**
     * 创建一个包含数据、错误码和自定义消息的错误响应结果。
     *
     * @param <T>        返回数据的类型
     * @param data       返回的数据
     * @param errorCode  错误码枚举
     * @param message    自定义消息说明
     * @return 封装了错误状态码、数据和自定义消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), data, message);
    }

    /**
     * 创建一个包含错误码和自定义消息的错误响应结果。
     *
     * @param <T>        返回数据的类型
     * @param errorCode  错误码枚举
     * @param message    自定义消息说明
     * @return 封装了错误状态码和自定义消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
