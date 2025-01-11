package com.caiths.caiapibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类，用于封装API的响应结果。
 *
 * <p>此类包含响应代码、响应数据和消息说明，提供多种构造方法方便使用。</p>
 *
 * @param <T> 响应数据的类型
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1693660536490703953L;

    /**
     * 响应代码，用于标识响应的状态。
     */
    private int code;

    /**
     * 响应数据，具体类型由泛型指定。
     */
    private T data;

    /**
     * 响应消息，描述响应的详细信息。
     */
    private String message;

    /**
     * 全参数构造方法，初始化所有字段。
     *
     * @param code 响应代码
     * @param data 响应数据
     * @param message 响应消息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 部分参数构造方法，初始化响应代码和数据，消息默认为空字符串。
     *
     * @param code 响应代码
     * @param data 响应数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 使用错误代码对象构造响应，仅初始化响应代码和消息，数据默认为null。
     *
     * @param errorCode 错误代码对象，包含代码和消息信息
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
