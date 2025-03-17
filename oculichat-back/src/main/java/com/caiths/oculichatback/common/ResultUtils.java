package com.caiths.oculichatback.common;

/**
 * 返回工具类，用于构建标准的 {@link BaseResponse} 响应对象。
 *
 * <p>该工具类提供了多种静态方法，以便在应用程序中统一处理成功和错误响应。</p>
 *
 * @author poboll
 */
public class ResultUtils {

    /**
     * 构建一个成功的响应。
     *
     * @param <T>  响应数据的类型
     * @param data 返回的数据
     * @return 一个包含数据和成功状态的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 构建一个错误的响应，基于指定的错误代码。
     *
     * @param <T>        响应数据的类型
     * @param errorCode 表示错误的 {@link ErrorCode} 枚举
     * @return 一个包含错误代码和默认错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 构建一个错误的响应，包含自定义数据和消息。
     *
     * @param <T>     响应数据的类型
     * @param data    返回的数据
     * @param message 错误消息
     * @return 一个包含数据和自定义错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, String message) {
        return new BaseResponse<>(202, data, message);
    }

    /**
     * 构建一个错误的响应，包含错误代码和消息。
     *
     * @param <T>     响应数据的类型
     * @param code    错误代码
     * @param message 错误消息
     * @return 一个包含错误代码和错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 构建一个错误的响应，基于指定的错误代码和数据。
     *
     * @param <T>        响应数据的类型
     * @param data       返回的数据
     * @param errorCode  表示错误的 {@link ErrorCode} 枚举
     * @return 一个包含错误代码、数据和默认错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), data, errorCode.getMessage());
    }

    /**
     * 构建一个错误的响应，基于指定的错误代码、数据和自定义消息。
     *
     * @param <T>        响应数据的类型
     * @param data       返回的数据
     * @param errorCode  表示错误的 {@link ErrorCode} 枚举
     * @param message    自定义的错误消息
     * @return 一个包含错误代码、数据和自定义错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(T data, ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), data, message);
    }

    /**
     * 构建一个错误的响应，基于指定的错误代码和自定义消息。
     *
     * @param <T>        响应数据的类型
     * @param errorCode  表示错误的 {@link ErrorCode} 枚举
     * @param message    自定义的错误消息
     * @return 一个包含错误代码和自定义错误消息的 {@link BaseResponse} 对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
