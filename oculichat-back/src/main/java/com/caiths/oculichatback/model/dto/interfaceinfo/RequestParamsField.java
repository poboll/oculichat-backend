package com.caiths.oculichatback.model.dto.interfaceinfo;

import lombok.Data;

/**
 * 请求参数字段数据传输对象。
 * <p>
 * 该类用于封装接口请求的参数信息，包括参数的标识符、名称、类型、描述以及是否为必填项等内容。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class RequestParamsField {

    /**
     * 请求参数的唯一标识符。
     */
    private String id;

    /**
     * 请求参数的名称。
     */
    private String fieldName;

    /**
     * 请求参数的数据类型（如 String、Integer 等）。
     */
    private String type;

    /**
     * 请求参数的描述信息，用于说明参数的功能或用途。
     */
    private String desc;

    /**
     * 请求参数是否为必填项（例如 "true" 或 "false"）。
     */
    private String required;
}
