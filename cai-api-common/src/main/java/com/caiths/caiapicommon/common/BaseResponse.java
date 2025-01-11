package com.caiths.caiapicommon.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类，用于封装接口响应的结果。
 *
 * <p>此类包含状态码、数据和消息，用于统一接口的返回格式。</p>
 *
 * @param <T> 返回数据的类型
 * @version 1.0
 * @date 2024-12-02
 * @author
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1693660536490703953L;

    /**
     * 状态码，表示响应的结果状态。
     */
    private int code;

    /**
     * 返回的数据内容。
     */
    private T data;

    /**
     * 响应的消息说明。
     */
    private String message;

    /**
     * 构造一个新的 BaseResponse 实例。
     *
     * @param code 状态码
     * @param data 返回的数据
     * @param message 消息说明
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 构造一个新的 BaseResponse 实例，默认消息为空。
     *
     * @param code 状态码
     * @param data 返回的数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 构造一个新的 BaseResponse 实例，基于错误码。
     *
     * @param errorCode 错误码枚举
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
