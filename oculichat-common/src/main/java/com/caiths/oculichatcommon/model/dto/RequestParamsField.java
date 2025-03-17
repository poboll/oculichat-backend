package com.caiths.oculichatcommon.model.dto;

import lombok.Data;

/**
 * 请求参数字段，表示接口请求中的单个参数信息。
 *
 * <p>此类包含参数的唯一标识、名称、类型、描述以及是否为必填项的信息。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class RequestParamsField {

    /**
     * 参数的唯一标识。
     */
    private String id;

    /**
     * 参数名称。
     */
    private String fieldName;

    /**
     * 参数类型，例如 String、Integer 等。
     */
    private String type;

    /**
     * 参数描述，说明该参数的用途。
     */
    private String desc;

    /**
     * 是否为必填项，通常为 "true" 或 "false"。
     */
    private String required;
}
