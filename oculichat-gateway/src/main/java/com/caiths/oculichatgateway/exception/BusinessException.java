package com.caiths.oculichatgateway.exception;

import com.caiths.oculichatcommon.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4593480471566176059L;
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
