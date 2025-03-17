package com.caiths.oculichatback.model.dto.interfaceinfo;

import lombok.Data;

/**
 * 响应参数字段数据传输对象。
 * <p>
 * 该类用于封装接口响应的参数信息，包括参数的标识符、名称、类型以及描述等内容。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class ResponseParamsField {

    /**
     * 响应参数的唯一标识符。
     */
    private String id;

    /**
     * 响应参数的名称。
     */
    private String fieldName;

    /**
     * 响应参数的数据类型（如 String、Integer 等）。
     */
    private String type;

    /**
     * 响应参数的描述信息，用于说明参数的功能或用途。
     */
    private String desc;
}
