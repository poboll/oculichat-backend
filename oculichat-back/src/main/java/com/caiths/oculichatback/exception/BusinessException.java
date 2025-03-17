package com.caiths.oculichatback.exception;

import com.caiths.oculichatback.common.ErrorCode;

/**
 * 自定义业务异常类，用于处理应用程序中的业务逻辑错误。
 * <p>
 * 该异常类扩展自 {@link RuntimeException}，包含错误码和错误消息，
 * 以便在应用程序中更精确地定位和处理不同类型的错误。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 2942420535500634982L;

    /**
     * 错误码，用于标识具体的业务错误类型。
     */
    private final int code;

    /**
     * 根据错误码和错误消息创建一个新的 {@link BusinessException} 实例。
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 根据 {@link ErrorCode} 创建一个新的 {@link BusinessException} 实例。
     * 使用 {@link ErrorCode#getCode()} 作为错误码，{@link ErrorCode#getMessage()} 作为错误消息。
     *
     * @param errorCode 错误码枚举对象
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 根据 {@link ErrorCode} 和自定义错误消息创建一个新的 {@link BusinessException} 实例。
     * 使用 {@link ErrorCode#getCode()} 作为错误码，自定义消息作为错误消息。
     *
     * @param errorCode 错误码枚举对象
     * @param message   自定义错误消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public int getCode() {
        return code;
    }
}
